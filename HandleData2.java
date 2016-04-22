import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleData2
{

	public static void main(String[] args) throws Exception
	{
		ExtractES(1, 1230);
	}

	public static void ExtractES(int game, int end) throws Exception
	{
		while (game <= end)
		{
			writeData(organiseData(extractLink(getLink(game))));
			System.out.println(game);
			game++;
		}

	}

	private static void writeData(String[][] data) throws IOException
	{
		for (int i = 1; i < 40 && data[i][0] != null; i++)
		{
			File file = new File("/home/noni/NHL/S15-16/" + data[i][0] + ".txt");
			int games = 0;
			if (file.exists())
			{
				String[] player = new String[23];
				int[] playerInt = new int[23];
				BufferedReader fr = new BufferedReader(new FileReader(file));
				games = Integer.parseInt(fr.readLine());
				for (int j = 1; j < 23; j++)
				{
					String line = fr.readLine();
					data[i][j] = (Integer.parseInt(line) + Integer
							.parseInt(data[i][j])) + "";
				}
				fr.close();
			}
			FileWriter fw = new FileWriter(file);
			fw.write((games + 1) + "\n");
			for (int j = 1; j < 23; j++)
			{
				if (data[i][j].equals(""))
				{
					data[i][j] = "0";
				}
				fw.write(Integer.parseInt(data[i][j]) + "\n");
			}
			fw.close();
		}
	}

	public static String[][] organiseData(String[] text)
	{
		String[][] data = new String[42][23];
		int playerNum = 0;
		for (int i = 0; i <= text.length && text[i] != null; i++)
		{
			if (text[i].equals("TEAM SUMMARY")
					|| ((data[0][0] != null) && (text[i].equals("EV"))))
			{
				if (data[0][0] != null && text[i].equals("EV"))
				{
					i += 2;
				}
				i++;
				int j = 0;
				for (; j < 20; j++)
				{
					for (int k = 0; k < 23; k++, i++)
					{
						if (text[i].equals("TOI"))
						{
							i += 11;
						}
						if (text[i].equals("EV"))
						{
							i -= 15;
						}
						
						data[playerNum + j][k] = text[i];
						if (text[i].equals(""))
						{
							data[playerNum + j][k] = "0";
						}
						if (data[playerNum + j][k].contains(":"))
						{
							data[playerNum + j][k] = ""
									+ convertTime(data[playerNum + j][k]);
						}

						if (text[i].equals("F%"))
						{
							i += 6;
							k = 23;
						}
					}
					if (text[i].equals("TEAM TOTALS"))
					{
						j = 21;
					}
					i += 2;
				}
				playerNum = j;
			}
		}
		data[0][0] = "Players";
		return data;
	}

	public static URL getLink(int game) throws Exception
	{
		URL link;
		if (game < 10)
		{
			link = new URL(
					"http://www.nhl.com/scores/htmlreports/20152016/ES02000"
							+ game + ".HTM");
		} else if (game < 100)
		{
			link = new URL(
					"http://www.nhl.com/scores/htmlreports/20152016/ES0200"
							+ game + ".HTM");
		} else if (game < 1000)
		{
			link = new URL(
					"http://www.nhl.com/scores/htmlreports/20152016/ES020"
							+ game + ".HTM");
		} else
		{
			link = new URL(
					"http://www.nhl.com/scores/htmlreports/20152016/ES02"
							+ game + ".HTM");
		}
		return link;
	}

	public static String[] extractLink(URL link) throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(
				link.openStream()));
		String[] text = new String[2000];
		String inputLine;
		String regex = "(>.*?<)";
		Pattern pattern = Pattern.compile(regex);
		int count = 0;
		while ((inputLine = in.readLine()) != null)
		{
			Matcher matcher = pattern.matcher(inputLine);
			if (matcher.find())
			{
				text[count] = matcher.group(1);
				text[count] = text[count].replaceAll("(&nbsp;)|[<>]?", "");
				count++;
			}
		}
		in.close();

		return text;
	}

	private static int convertTime(String time)
	{
		String[] units = time.split(":"); // will break the string up into an
											// array
		int minutes = Integer.parseInt(units[0]); // first element
		int seconds = Integer.parseInt(units[1]); // second element
		int duration = 60 * minutes + seconds; // add up our values
		return duration;
	}

	private static void check(String s) throws FileNotFoundException{
		File file =new File("/home/noni/NHL/S15-16/" + s
				+ ".txt");
		Scanner input = new Scanner(file);
		while(input.hasNextLine()){
			System.out.println(input.nextLine());
		}
	}
}
