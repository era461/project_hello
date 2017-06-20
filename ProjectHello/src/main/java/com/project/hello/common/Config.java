package com.project.hello.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

/**
 * 어노테이션 기법으로 설정
 * Property 객체 사용하여 이용할경우 변경 필요
 * 필요한 항목 추가 및 불필요한 항목 제거
 * @author YOONHWAN
 *
 */
@Controller
public class Config {
	
	public static String GLOBALS_SHELLFILEPATH          ;
	
	public static String GLOBALS_SERVERCONFPATH         ;
	
	public static String GLOBALS_CLIENTCONFPATH         ;
	
	public static String GLOBALS_FILEFORMATPATH         ;
	
	public static String GLOBALS_GPKICONFPATH           ;
	
	public static String GLOBALS_CONFPATH               ;
	
	public static String GLOBALS_MAILREQUESTPATH        ;
	
	public static String GLOBALS_MAILRRESPONSEPATH      ;
	
	public static String GLOBALS_SMECONFIGPATH          ;
	
	public static String GLOBALS_FILESTOREPATH          ;
	
	public static String GLOBALS_SYNCHRNSERVERPATH      ;

	@Value("#{props['Globals.ShellFilePath']}")
	public void setGLOBALS_SHELLFILEPATH(String gLOBALS_SHELLFILEPATH) {
		GLOBALS_SHELLFILEPATH = gLOBALS_SHELLFILEPATH;
	}

	@Value("#{props['Globals.ServerConfPath']}")
	public void setGLOBALS_SERVERCONFPATH(String gLOBALS_SERVERCONFPATH) {
		GLOBALS_SERVERCONFPATH = gLOBALS_SERVERCONFPATH;
	}

	@Value("#{props['Globals.ShellFilePath']}")
	public void setGLOBALS_CLIENTCONFPATH(String gLOBALS_CLIENTCONFPATH) {
		GLOBALS_CLIENTCONFPATH = gLOBALS_CLIENTCONFPATH;
	}

	@Value("#{props['Globals.ClientConfPath']}")
	public  void setGLOBALS_FILEFORMATPATH(String gLOBALS_FILEFORMATPATH) {
		GLOBALS_FILEFORMATPATH = gLOBALS_FILEFORMATPATH;
	}

	@Value("#{props['Globals.ShellFilePath']}")
	public  void setGLOBALS_GPKICONFPATH(String gLOBALS_GPKICONFPATH) {
		GLOBALS_GPKICONFPATH = gLOBALS_GPKICONFPATH;
	}

	@Value("#{props['Globals.GPKIConfPath']}")
	public void setGLOBALS_CONFPATH(String gLOBALS_CONFPATH) {
		GLOBALS_CONFPATH = gLOBALS_CONFPATH;
	}

	@Value("#{props['Globals.ShellFilePath']}")
	public void setGLOBALS_MAILREQUESTPATH(String gLOBALS_MAILREQUESTPATH) {
		GLOBALS_MAILREQUESTPATH = gLOBALS_MAILREQUESTPATH;
	}

	@Value("#{props['Globals.MailRequestPath']}")
	public void setGLOBALS_MAILRRESPONSEPATH(String gLOBALS_MAILRRESPONSEPATH) {
		GLOBALS_MAILRRESPONSEPATH = gLOBALS_MAILRRESPONSEPATH;
	}

	@Value("#{props['Globals.SMEConfigPath']}")
	public void setGLOBALS_SMECONFIGPATH(String gLOBALS_SMECONFIGPATH) {
		GLOBALS_SMECONFIGPATH = gLOBALS_SMECONFIGPATH;
	}

	@Value("#{props['Globals.fileStorePath']}")
	public void setGLOBALS_FILESTOREPATH(String gLOBALS_FILESTOREPATH) {
		GLOBALS_FILESTOREPATH = gLOBALS_FILESTOREPATH;
	}

	@Value("#{props['Globals.SynchrnServerPath']}")
	public void setGLOBALS_SYNCHRNSERVERPATH(String gLOBALS_SYNCHRNSERVERPATH) {
		GLOBALS_SYNCHRNSERVERPATH = gLOBALS_SYNCHRNSERVERPATH;
	}
}