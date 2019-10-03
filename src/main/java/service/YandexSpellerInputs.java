package service;

public class YandexSpellerInputs {
    public static final String TEXT = "text";
    public static final String LANG = "lang";
    public static final String OPTIONS = "options";
    public static final String FORMAT = "format";

    public enum Language {
        RU("ru"),
        UK("uk"),
        EN("en");
        private String lang;
        public String getLanguage() {return lang;}
        Language(String lang) {
            this.lang = lang;
        }
    }

    public enum Format {
        PLAIN("plain"),
        HTML("html");
        private String formatCode;
        public String getFormat() {return formatCode;}
        Format(String format) {
            this.formatCode = format;
        }
    }






}
