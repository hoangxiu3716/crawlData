package de.gimik.apps.parsehub.backend.util;

public class Constants {
    // ~ Static fields/initializers
    // =============================================

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    public static final String DATA_CACHE_CONFIG_KEY = "configKey";

    /**
     * New User
     */
    public static final long NEW_USER = 1;

    /**
     * Old User
     */
    public static final long OLD_USER = 0;

    /**
     * Days for password expire
     */
    public static final String MAXIMUM_PASSWORD_AGE = "MAXIMUM_PASSWORD_AGE";

    /**
     * Days for warning
     */
    public static final String PASSWORD_ALERT_THRESHOLD = "PASSWORD_ALERT_THRESHOLD";

    /**
     * The name of the ADMIN role
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_EMPLOYEES = "ROLE_EMPLOYEES";
    public static final String ROLE_AGENT = "ROLE_AGENT";
    public static final String ROLE_AGENCY = "ROLE_AGENCY";
    

    public static final String ANONYMOUS = "ANONYMOUS";
    public static final String ROOMPICTURE = "room_picture";

    public static final int MAX_RESULT = 10;

    public static final String DATE_FORMAT = "yyyy/MM/dd";

    public static final String DATE_FORMAT_FOR_VALIDATION = "yyyyMMdd";

    public static final int MIN_PASWORD_LENGTH = 4;

    public static final int MAX_PASWORD_LENGTH = 20;

    public static final int PASSWORD_EXPIRED_FLAG = 2;

    public static final String MESSAGE_CODE_TEMPLATE = "%1$s(%2$d)";

    public static final String MSG_CODE_TEMPLATE_IDENTICAL = "%1$s";

    public static final String NA = "label.na";

    public static final long IMAGE_UPLOAD_FILE_MAX_SIZE = 500 * 1024 * 1024;

    public static final String ENGLISH = "English";
    public static final String GERMAN = "German";
    
    public static final String ERROR = "error";
    public static final Integer OK = 200;
    public interface ErrorCode {
        public static final String NOT_AVAILABLE_FUNCTION = "ERR_0001";
        public static final String UNKNOWN_ERROR = "ERR_0002";
        public static final String IMAGE_UPLOAD_ERROR = "ERR_0003";
        public static final String CENTER_NOT_SELECTED = "ERR_0004";
        public static final String FROM_DATE_MUST_BEFORE_TO_DATE = "ERR_0005";

        public static final String USERNAME_DUPLICATED = "ERR_1001";
        public static final String USER_ROLE_NOT_SET = "ERR_1002";
        public static final String USERNAME_NOT_EXIST = "ERR_1003";
        public static final String PASSWORD_INVALID = "ERR_1004";
        public static final String ABBREVIATION_DUPLICATED = "ERR_1005";

        public static final String NAME_DUPLICATE = "ERR_1101";
        public static final String ROOM_INFO_DUPLICATE = "ERR_1102";
        public static final String PROJECT_NUMBER_EXIST = "ERR_1103";
        public static final String NAME_WETTPORTAL_DUPLICATE = "name Wettportal duplicate";
        public static final String NAME_FLASHSCORE_DUPLICATE = "name Flashscore duplicate";
        public static final String TEAMS_NOT_EXIST = "teams not exist";
        public static final String BAD_TOKEN = "bad token!!";
        

    }

    public interface ImageFolder {

    }
    public interface folder {
    	public static final String SCREENSHOT ="screenshot";
    	public static final String GARBAGE_EXCEL ="garbageexcel";
    }
    public interface ExprotType {
        public static final String PDF = "pdf";
    }

    public interface Object {
        public static final String USER = "User";
        public static final String ROLE = "Role";
        public static final String PROFILE = "Profile";
        public static final String CUSTOMER = "customer";
        public static final String EMPLOYEES = "employees";
        public static final String WINGO = "wingo";
        public static final String TEAMS = "teams";
        public static final String GROUP_PHARMA = "group_pharma";
        public static final String BASE_PHARMA = "base_pharma";
        public static final String CROSS_SELLING = "CROSS-SELLING";
    }

    public interface Action {
        public static final String ADD = "Add";
        public static final String DELETE = "Delete";
        public static final String UPDATE = "Update";
        public static final String ACTIVATE = "Activate";
        public static final String DEACITIVATE = "Deactivate";
        public static final String CHANGE_PASSWORD = "Change Password";
        public static final String DELETE_All = "Delete all";
//		public static final String SEND_EMAIL = "Send Email";
    }

    public interface CategoryType {
        public static final int FAMILY = 1;
        public static final int SOCIAL = 2;
        public static final int AGE = 3;
        public static final int TYPE = 4;
        public static final int GENDER = 5;
    }

    public interface Role {

        public static final Long USER = 1L;

        public static final Long ADMIN = 2L;

        public static final Long PHYSICIAN = 3L;

    }

    public interface Sex {
        public static final int MALE = 1;
        public static final int FEMALE = 2;
    }

    /**
     * Error message key
     */
    public interface ERROR_MESSAGE {

        public static final String KEY_INPUT_REQUIRED = "errors.required";
        public static final String KEY_INPUT_INVALID = "errors.invalid";
        public static final String NOT_EXCEL_FILE = "Please import xlsx file";
        public static final String BAD_USERNAME_AND_PASSWORD = "wrong username or password invalid";
        public static final String ClaimDirectFzgTyp_not_exist = "ClaimDirectFzgTyp not exist";
        public static final String ClaimDirectStvsDekra_not_exist = "ClaimDirectStvsDekra not exist";
        public static final String KEY_INPUT_REQUIRED_MESSAGE = "Some data input rerquired";
        public static final String KEY_INPUT_DONT_EXIST = "errors";
        public static final String GROUP_HAVE_BEEN_DELETED = "group have been deleted";
        public static final String BAD_TOKEN = "bad token!!";
  
    }

    /**
     * Error argument key
     */

    public interface ERROR_ARGUMENT {

        public static final String USERNAME = "user.username";
        public static final String PASSWORD = "user.password";


    }

    /**
     * Max length of username attribute.
     */
    public static final int USERNAME_MAXLENGTH = 50;

    /**
     * Min length of username attribute.
     */
    public static final int USERNAME_MINLENGTH = 2;

    /**
     * Max length of loginName attribute.
     */
    public static final int LOGINNAME_MAXLENGTH = 20;

    /**
     * Min length of loginName attribute.
     */
    public static final int LOGINNAME_MINLENGTH = 1;


    /**
     * Max length of password attribute.
     */
    public static final int PASSWORD_MAXLENGTH = 20;

    /**
     * Min length of password attribute.
     */
    public static final int PASSWORD_MINLENGTH = 4;

    /**
     * default value of Character variable
     */
    public static final Character DEFAULT_CHAR_VALUE = '0';

    /**
     * empty string
     */
    public static final String DEFAULT_STRING_VALUE = "";

    /**
     * Commas character.
     */
    public static final char COMMA = ',';

    /**
     * Commas character.
     */
    public static final char APOSTROPHE = '\'';

    /**
     * Commas character.
     */
    public static final char SPACE = ' ';
    public static final char DASH = '-';

    /**
     * quotation marks
     */
    public static final char QUOTATION_MARKS = '\"';

    public static final String PHONE_NUMBER_SEPARATOR = " / ";
    
    
    public interface type{
    	public static final String SPATIAL_USE ="spatial_use";
    	public static final String TEST_METHOD ="test_method";
    	public static final String PROTECTIVE_CLASS ="protective_class";
    	public static final String CATEGORIES_LEVEL1 ="categories_level1";
    	public static final String CATEGORIES_LEVEL2 ="categories_level2";
    	
    	public static final String BUILDING ="building";
    	public static final String FLOOR ="floor";
    }
    public static final Integer ROOM_STATUS_NOT_START = 0;
    public static final Integer ROOM_STATUS_NOT_INPROGRESS = 1;
    public static final Integer ROOM_STATUS_DONE = 2;
    public static final Integer ROOM_STATUS_CROSSCHECK = 4;
    public static final Integer ROOM_STATUS_HANDOVER = 5;
    public static final Integer TEST_METHOD_STATUS_DONE = 3;
    public static final Integer COUNT_EMPTY = 0;

    public static final String SUCCESS = "Success";
    public interface Header {
    	public static final String Schadeninformation ="Schadeninformation";
    	public static final String Schadenzusatzkosten ="Schadenzusatzkosten";
    	public static final String Schadenhergang ="Schadenhergang";
    	public static final String Schadensteuerung ="Schadensteuerung";
    	public static final String Datenumfang ="Datenumfang";
    	public static final String Zahlschaden ="Laufleistung zu Schadenhöhe";
    	public static final String Betrug="Betrug";
    	
    }
    public interface TOOLID {
    	public static final String PHARMA_TOOL ="PHARMA_TOOL";
    	public static final String PARSEHUB_TOOL ="PARSEHUB_TOOL";
    	
    }
    public interface Keyword {
        public static final String KAT1 = "kat1";
        public static final String KAT_1 = "kat_1";
        public static final String KAT2 = "kat2";
        public static final String KAT_2 = "kat_2";
        public static final String diabetes_teststreifen = "diabetes_teststreifen";
        public static final String dia_teststreifen = "dia_teststreifen";
        public static final String Diabetes = "Diabetes";
    }
    public interface Code {
    	public static final String SANICARE ="SANICARE";
    	public static final String APOTHEKE ="APOTHEKE";
    	public static final String JUVALIS ="JUVALIS";
    	public static final String EURAPON ="EURAPON";
    	public static final String APODISCOUNTER ="APODISCOUNTER";
    	public static final String MEDPEX ="MEDPEX";
    	public static final String MEDIKAMENTE ="MEDIKAMENTE";
    	public static final String DOCMORRIS ="DOCMORRIS";
    	public static final String APONEO ="APONEO";
    	public static final String APOTAL ="APOTAL";
    	public static final String MYCARE ="MYCARE";
    }
    public interface Shop {
        public static final Integer SHOP_APOTHEKE_FROM_PHARMA = 22;
        public static final Integer SHOP_SANICARE_FROM_PHARMA = 21;
    	
    }
    public interface CrawlType {
    	 public static final String KEYWORD = "KEYWORD";
         public static final String CATEGORY = "CATEGORY";
         public static final String CROSS_SELLING = "CROSS-SELLING";
    }
}
