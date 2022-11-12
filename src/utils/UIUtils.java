package utils;

import api.Command;

import java.util.Map;

public class UIUtils {
    public static final String HELP_DESCRIPTION = "Эта команда позволит вам узнать, какие ещё команды существуют.";
    public static final String LOADFILE_DESCRIPTION = "Эта команда позволит выбрать файл и открыть его.";
    public static final String SEARCH_DESCRIPTION = "Эта команда позволит найти запись из файла по ключу";
    public static final String ADDUSER_DESCRIPTION = "Эта команда позволит добавить запись";
    public static final String REMOVEUSER_DESCRIPTION = "Эта команда позволит удалит запись из файла";
    public static final String NEWFILE_DESCRIPTION = "Эта команда позволит создать новый файл";
    public static final String EXIT_DESCRIPTION = "Эта команда завершит работу приложения.";
    public static final Map<Command, String> COMMAND_TO_DESCRIPTION_MAP = Map.of(
            Command.HELP, HELP_DESCRIPTION,
            Command.LOADFILE, LOADFILE_DESCRIPTION,
            Command.SEARCH, SEARCH_DESCRIPTION,
            Command.ADDUSER, ADDUSER_DESCRIPTION,
            Command.REMOVEUSER, REMOVEUSER_DESCRIPTION,
            Command.NEWFILE, NEWFILE_DESCRIPTION,
            Command.EXIT, EXIT_DESCRIPTION);
}

