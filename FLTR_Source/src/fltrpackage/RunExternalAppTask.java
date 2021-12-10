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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.SwingWorker;

class RunExternalAppTask extends SwingWorker<Void, Void> {

	private String command;

	public RunExternalAppTask(String command, String searchword) {
		super();
		this.command = command.replace(Constants.TERM_PLACEHOLDER, "\"" + searchword + "\"");
	}

	@Override
	public Void doInBackground() {
		ResultFrame rf = FLTR.getResultFrame();
		if (rf == null) {
			rf = new ResultFrame();
			FLTR.setResultFrame(rf);
		}
		boolean waiting = true;
		int commandLength = command.length();
		int l = Constants.EXEC_NOWAIT_END_MARKER.length();
		if (command.substring(commandLength - l).equals(Constants.EXEC_NOWAIT_END_MARKER)) {
			waiting = false;
			command = command.substring(0, commandLength - l).trim();
		}
		String[] commandarray = command.split("\\|");
		ArrayList<String> pcommand = new ArrayList<String>();
		for (String p : commandarray) {
			p = p.trim();
			if (p.startsWith("\"") && p.endsWith("\"")) {
				p = p.substring(1, p.length() - 1);
				p = p.replace("\\\"", "\"");
			}
			if (!p.equals(""))
				pcommand.add(p);
		}
		StringBuilder output = new StringBuilder();
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(pcommand);
		try {
			output.append("Running task: " + processBuilder.command().toString() + (waiting ? " <" : " <NON-")
					+ "WAITING> …\n\n");
			rf.putTextIntoTfResults(output.toString());
			Process process = processBuilder.start();
			if (waiting) {
				rf.getButKill().setEnabled(true);
				rf.setProcess(process);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
					rf.putTextIntoTfResults(output.toString());
				}
				int exitVal = process.waitFor();
				output.append("\n… ended with return code " + String.valueOf(exitVal) + ".\n");
			} else {
				output.append("No output and exit code will be shown here for non-waiting tasks.\n"
						+ "You may minimize this window while working with the text.\n");
			}
		} catch (Exception e) {
			output.append("\n… ended with exception \"" + e.getMessage() + "\".\n");
		}
		rf.putTextIntoTfResults(output.toString());
		rf.getButKill().setEnabled(false);
		rf.setProcess(null);
		return null;
	}

	@Override
	public void done() {
		ResultFrame rf = FLTR.getResultFrame();
		if (rf != null) {
			rf.getButKill().setEnabled(false);
			rf.setProcess(null);
		}
	}

}
