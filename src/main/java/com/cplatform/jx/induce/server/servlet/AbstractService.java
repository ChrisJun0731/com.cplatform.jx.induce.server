package com.cplatform.jx.induce.server.servlet;

import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.cache.TransactonIdCache;

public abstract class AbstractService<E, T> {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	/** sysConfig */
	@Resource(name = "sysConfig")
	public PropertiesConfiguration sysConfig;

	/** transactonIdCache */
	@Autowired
	private TransactonIdCache transactonIdCache;

	protected abstract boolean needCheckSignature();

	/**
	 * @return 是否需要做事务验证
	 */
	protected abstract boolean needCheckTransaction();

	@RequestMapping()
	public void process(//
	        @RequestHeader(value = "ts-transaction-id", required = false)//
	        String transactionId,//
	        @RequestHeader(value = "ts-signature", required = false)//
	        String signature, //
	        InputStream inputStream, //
	        HttpServletRequest httpRequest, //
	        HttpServletResponse httpResponse) throws Exception {
		//
		byte[] requestBody = IOUtils.toByteArray(inputStream);
		//
		String requestText = IOUtils.toString(requestBody, "GB18030");
		logger.info("收到报文: " + requestText);
		// 验证签名
		if (needCheckSignature() == true) {
			if (requestBody != null && requestBody.length > 0 //
			        && "7facc1ab926032af439147798e60a360".equals(signature) == false) {
				String sign = DigestUtils.md5Hex(requestBody);
				if (sign.equalsIgnoreCase(signature) == false) {
					logger.info("签名验证异常, sign.req=" + signature + ", sign.gen=" + sign + ", text=" + requestText);
					httpResponse.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
					return;
				}
			}
		}
		// 验证事务Id，防止重发
		if (needCheckTransaction()) {
			String cachedValue = transactonIdCache.check(transactionId);
			if (cachedValue != null) {
				logger.info("相同事务ID，返回缓存结果, TransactionId=" + transactionId + ", cachedValue=" + cachedValue);
				httpResponse.getOutputStream().write(cachedValue.getBytes("GB18030"));
				httpResponse.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
				return;
			}
		}
		//
		E requestObject = requestJsonToObject(requestText);
		//
		logger.info(requestObject);
		//
		T response = processRequest(requestObject);
		//
		String responseText = JSONObject.fromObject(response).toString();
		//
		logger.info("请求处理结果: " + responseText);
		// 记录事务Id对应的处理结果
		if (needCheckTransaction() == true) {
			transactonIdCache.set(transactionId, responseText);
		}
		// 返回结果
		if (responseText != null) {
			byte[] responseData = responseText.getBytes("GB18030");
			String sign = DigestUtils.md5Hex(responseData);
			httpResponse.setHeader("ts-signature", sign);
			httpResponse.getOutputStream().write(responseData);
		}
		//
		httpResponse.setStatus(HttpURLConnection.HTTP_OK);
	}

	public abstract T processRequest(E request);

	protected abstract E requestJsonToObject(String jsonString) throws Exception;

	protected <K> K requestJsonToObject(String jsonString, Class<K> objectClass) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		@SuppressWarnings("unchecked")
		K request = (K) JSONObject.toBean(jsonObject, objectClass);
		return request;
	}
}
