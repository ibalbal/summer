package com.yujigu.summer.generator;


import com.symxns.sym.generator.SymGenerator;

/**
 * @author ly
 * @create 2020-10-12-14:23
 */
public class GeneratorMyBatisPlus {

    private static String TABLES = "all";

    private static String BASE_PATH = "C:\\data\\work\\idea\\summer";

//    private static String BASE_PATH = "/Users/y/IdeaProjects/ibalbal/xlsystem";
//    private static String BASE_PATH = "D:/LY/xlsystem";
    private static String OUT_PATH = BASE_PATH + "/summer-server-oauth/oauth-summer-system";
    private static String OUT_PATH_SERVICE = BASE_PATH + "/summer-server-oauth/oauth-summer-system";
    private static String OUT_PATH_CONTROLLER = BASE_PATH +  "/summer-server-oauth/oauth-summer-admin";
    private static String OUT_PATH_ENTITY = BASE_PATH +  "/summer-common";
    private static String PACKAGE = "com.yujigu.summer";
    private static String PACKAGE_MAIN = "oauth";
    private static String PACKAGE_ENTITY = "common";
    private static String DATA_USER_NAME = "root";
    private static String DATA_PASSWORD = "Got#J1^7QQKHEH*b";
    private static String DATA_URL = "jdbc:mysql://192.168.10.2:3306/summer_oauth?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai";


    public static void main(String[] args) {
        SymGenerator.build(DATA_URL, DATA_USER_NAME, DATA_PASSWORD, OUT_PATH, PACKAGE, PACKAGE_MAIN, PACKAGE_ENTITY, TABLES, OUT_PATH_ENTITY,OUT_PATH_SERVICE, OUT_PATH_CONTROLLER, true);
    }
}
