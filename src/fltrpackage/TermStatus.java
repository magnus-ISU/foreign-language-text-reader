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

import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Vector;

public enum TermStatus {

	New(0), Unknown(1), Learning2(2), Learning3(3), Learning4(4), Known(5), Ignored(98), WellKnown(99);

	private static final HashMap<Integer, TermStatus> lookup = new HashMap<Integer, TermStatus>();

	private static final HashMap<Integer, TermStatus> lookupOrdinal = new HashMap<Integer, TermStatus>();

	private static final HashMap<TermStatus, String> texts = new HashMap<TermStatus, String>();

	private static final HashMap<TermStatus, String> shorttexts = new HashMap<TermStatus, String>();

	static {
		for (TermStatus status : EnumSet.allOf(TermStatus.class)) {
			TermStatus.lookup.put(status.statusCode, status);
			TermStatus.lookupOrdinal.put(status.ordinal(), status);
		}

		TermStatus.texts.put(New, "No status (" + New.getStatusCode() + ")");
		TermStatus.texts.put(Unknown, "Unknown (" + Unknown.getStatusCode() + ")");
		TermStatus.texts.put(Learning2, "Learning (" + Learning2.getStatusCode() + ")");
		TermStatus.texts.put(Learning3, "Learning (" + Learning3.getStatusCode() + ")");
		TermStatus.texts.put(Learning4, "Learning (" + Learning4.getStatusCode() + ")");
		TermStatus.texts.put(Known, "Known (" + Known.getStatusCode() + ")");
		TermStatus.texts.put(Ignored, "Ignored (" + Ignored.getStatusCode() + ")");
		TermStatus.texts.put(WellKnown, "Well Known (" + WellKnown.getStatusCode() + ")");

		TermStatus.shorttexts.put(New, "No Status");
		TermStatus.shorttexts.put(Unknown, "Unknown/1");
		TermStatus.shorttexts.put(Learning2, "Learning/2");
		TermStatus.shorttexts.put(Learning3, "Learning/3");
		TermStatus.shorttexts.put(Learning4, "Learning/4");
		TermStatus.shorttexts.put(Known, "Known/5");
		TermStatus.shorttexts.put(Ignored, "Ignored");
		TermStatus.shorttexts.put(WellKnown, "Well Known");
	}

	public static Vector<TermStatus> getAllActive() {
		Vector<TermStatus> r = new Vector<TermStatus>(7);
		for (TermStatus status : EnumSet.allOf(TermStatus.class)) {
			if (status != New) {
				r.add(status);
			}
		}
		r.trimToSize();
		return r;
	}

	public static String getAllStatuses() {
		Vector<TermStatus> all = TermStatus.getAllActive();
		String s = "|";
		for (TermStatus ts : all) {
			s += String.valueOf(ts.statusCode) + "|";
		}
		return s;
	}

	public static Vector<ComboBoxItem> getComboBoxItemVector() {
		Vector<ComboBoxItem> r = new Vector<ComboBoxItem>(7);
		for (TermStatus status : EnumSet.allOf(TermStatus.class)) {
			if (status != New) {
				r.add(new ComboBoxItem(status.getStatusText(), 1000));
			}
		}
		r.trimToSize();
		return r;
	}

	public static TermStatus getStatusFromCode(int i) {
		if (TermStatus.lookup.containsKey(i)) {
			return TermStatus.lookup.get(i);
		} else {
			return Unknown;
		}
	}

	public static TermStatus getStatusFromOrdinal(int i) {
		if (TermStatus.lookupOrdinal.containsKey(i)) {
			return TermStatus.lookupOrdinal.get(i);
		} else {
			return Unknown;
		}
	}

	private final int statusCode;

	TermStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Color getStatusColor() {
		switch (this) {
		case New:
			return Color.decode("#" + Preferences.getCurrColorNew());
		case Unknown:
			return Color.decode("#" + Preferences.getCurrColorUnknown());
		case Learning2:
			return Color.decode("#" + Preferences.getCurrColorLearning2());
		case Learning3:
			return Color.decode("#" + Preferences.getCurrColorLearning3());
		case Learning4:
			return Color.decode("#" + Preferences.getCurrColorLearning4());
		case Known:
			return Color.decode("#" + Preferences.getCurrColorKnown());
		case Ignored:
			return Color.decode("#" + Preferences.getCurrColorIgnored());
		case WellKnown:
			return Color.decode("#" + Preferences.getCurrColorWellKnown());
		}
		return Color.WHITE;
	}

	public String getStatusShortText() {
		if (TermStatus.texts.containsKey(this)) {
			return TermStatus.shorttexts.get(this);
		} else {
			return "???";
		}
	}

	public String getStatusText() {
		if (TermStatus.texts.containsKey(this)) {
			return TermStatus.texts.get(this);
		} else {
			return "???";
		}
	}

}
