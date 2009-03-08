/*
 * @(#)UserStatus.java
 *
 * Copyright (c) 2001-2002, JangHo Hwang
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 	1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * 	2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * 	3. Neither the name of the JangHo Hwang nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *    $Id: UserStatus.java,v 1.3 2004/12/24 22:05:52 xrath Exp $ 
 */
package rath.msnm;
/**
 * ����msn��״̬ö�١�
 * ����Ϊ��enum���ӵĺ���
 * @author sheng.liuzs@alipay.com
 * @version $Id: UserStatus.java, v 0.1 2008-10-4 ����02:13:16 sheng.liuzs@alipay.com Exp $
 */
public interface UserStatus
{
	public static final String ONLINE = "NLN";
	public static final String OFFLINE = "FLN";
	public static final String INVISIBLE = "HDN";

	public static final String BUSY = "BSY";
	public static final String IDLE = "IDL";
	public static final String BE_RIGHT_BACK = "BRB";
	public static final String AWAY_FROM_COMPUTER = "AWY";
	public static final String ON_THE_PHONE = "PHN";
	public static final String ON_THE_LUNCH = "LUN";
};
