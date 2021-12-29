/*
 *
 * Foreign Language Text Reader (FLTR) - A Tool for Language Learning.
 *
 * Copyright © 2012-2021 FLTR Developers et al.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package fltrpackage;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Constants {

	public static final String SHORT_VERSION = "1.4.0";
	public static final String VERSION = Constants.SHORT_VERSION + " (2021-06-14)";

	public static final String SHORT_NAME = "FLTR";
	public static final String LONG_NAME = "Foreign Language Text Reader";
	public static final String WEBSITE = "https://sourceforge.net/projects/foreign-language-text-reader/";
	public static final String COPYRIGHT = "Copyright © 2012-2021 " + Constants.SHORT_NAME + " Developers et al.";

	public static final String ICONPATH = "/fltrpackage/icon128.png";
	public static final String HEADER_HTML_PATH = "/fltrpackage/_Header.htm";
	public static final String VOCAB_HTML_PATH = "/fltrpackage/_Vocabulary.htm";
	public static final String TEXT_HTML_PATH = "/fltrpackage/_Text.htm";

	public static final String OS_STRING = System.getProperty("os.name").toLowerCase();
	public static final OS_T OS = (OS_STRING.indexOf("win") >= 0) ? OS_T.WINDOWS :
			(OS_STRING.indexOf("mac") >= 0) ? OS_T.MACOS :
			(OS_STRING.indexOf("nux") >= 0 || OS_STRING.indexOf("nix") >= 0) ? OS_T.LINUX :
			OS_T.UNKNOWN; // Assuming this is BSD or something, should probably also be linux but will leave for now

	public static final String DATA_DIR =
		System.getenv("XDG_DATA_HOME") != null ? System.getenv("XDG_DATA_HOME") :
		Files.isDirectory(Paths.get(System.getProperty("user.home"), "/.local/share")) ? System.getProperty("user.home") + "/.local/share" :
		System.getProperty("user.home");
	public static final String LOCK_FILE_PATH = DATA_DIR + "/.foreign-language-text-reader-lock";
	public static final String PREF_FILE_PATH = DATA_DIR + "/.foreign-language-text-reader-prefs";

	public static final String TEXT_DIR_SUFFIX = "_Texts";
	public static final int TEXT_DIR_SUFFIX_LENGTH = Constants.TEXT_DIR_SUFFIX.length();

	public static final String LANG_SETTINGS_FILE_SUFFIX = "_Settings.csv";
	public static final int LANG_SETTINGS_FILE_SUFFIX_LENGTH = Constants.LANG_SETTINGS_FILE_SUFFIX.length();

	public static final String WORDS_FILE_SUFFIX = "_Words.csv";
	public static final String EXPORT_WORDS_FILE_SUFFIX = "_Export.txt";

	public static final String TEXT_FILE_EXTENSION = ".txt";
	public static final int TEXT_FILE_EXTENSION_LENGTH = Constants.TEXT_FILE_EXTENSION.length();

	public static final String VOCAB_FILE_NAME = "<Vocabulary>";

	public static final int MAX_DATA_LENGTH_START_FRAME = 35;
	public static final int MAX_TEXT_LENGTH_START_FRAME = 30;
	public static final int MAX_LANG_LENGTH_START_FRAME = 30;

	public static final double FUZZY_THRESHOLD = 0.333;

	public static final String PARAGRAPH_MARKER = "¶";

	public static final String ENCODING = "UTF-8";

	public static final String TAB = "\t";
	public static final String EOL = OS == OS_T.WINDOWS ? "\r\n" : "\n";
	public static final String UNIX_EOL = "\n";
	public static final String TERMS_SEPARATOR = " · ";
	public static final String URL_BEGIN_1 = "http://";
	public static final String URL_BEGIN_2 = "https://";
	public static final String EXEC_NOWAIT_END_MARKER = "&"; // Shell command (non-waiting)
	public static final String TERM_PLACEHOLDER = "###";

	public enum OS_T {
		WINDOWS,
		MACOS,
		LINUX,
		UNKNOWN,
	}
}
