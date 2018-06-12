package com.example.timekeeper.util;

/**
 * Created by Administrator on 2018/5/15.
 */

public class URL {
    public static final String IP_AND_PORT = "http://117.34.105.157:8080/";

    public static  final String LOGIN_URL = IP_AND_PORT+"timepicker/servlet/loginServlet";

    public static  final String REG_URL = IP_AND_PORT+"timepicker/servlet/registServlet";

    public static final String GET_ALL_Actions = IP_AND_PORT+"timepicker/servlet/showActivityServlet";

    public static final String GET_ONE_Action = IP_AND_PORT+"timepicker/servlet/findActivityDetailServlet";

    public static final String GET_ONE_Action_Person = IP_AND_PORT+"timepicker/servlet/findParticipantServlet";

    public static final String ADD_Action = IP_AND_PORT+"timepicker/servlet/createActivityServlet";

    public static final String GET_Select_Acion = IP_AND_PORT+"timepicker/servlet/findActivityDetailServlet";

    public static final String Change_Level = IP_AND_PORT+"timepicker/servlet/";

    public static final String TAKE_Action_Post = IP_AND_PORT+"timepicker/servlet/participantServlet";

    public static final String Share_Action = IP_AND_PORT+"timepicker/servlet/openAppServlet";

}
