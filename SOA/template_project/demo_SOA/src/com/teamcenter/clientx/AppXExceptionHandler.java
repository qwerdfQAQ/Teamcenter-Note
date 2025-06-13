
package com.teamcenter.clientx;

import com.teamcenter.schemas.soa._2006_03.exceptions.ConnectionException;
import com.teamcenter.schemas.soa._2006_03.exceptions.InternalServerException;
import com.teamcenter.schemas.soa._2006_03.exceptions.ProtocolException;
import com.teamcenter.soa.client.ExceptionHandler;
import com.teamcenter.soa.exceptions.CanceledOperationException;

/**
 * Implementation of the ExceptionHandler. For ConnectionExceptions (server
 * temporarily down .etc) prompts the user to retry the last request. For other
 * exceptions convert to a RunTime exception.
 */
public class AppXExceptionHandler implements ExceptionHandler {
	public void handleException(InternalServerException ise) {
		if (ise instanceof ConnectionException) {
			throw new RuntimeException("与Teamcenter服务器连接超时，请确认Teamcenter客户端配置是否正确");
		}
		if (ise instanceof ProtocolException) {
			throw new RuntimeException("与Teamcenter服务器连接发生错误，可能是由于连接协议使用不正确");
		}
		throw new RuntimeException(ise.getMessage());
	}

	public void handleException(CanceledOperationException coe) {
		throw new RuntimeException(coe);
	}

}
