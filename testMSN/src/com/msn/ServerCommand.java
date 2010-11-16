package com.msn;

public class ServerCommand {
	// The command name of the response
	private String _cmdID;
	// The entire response
	private String _line;
	// The parameters of the response (without transactionID, and divided with a space)
	private String[] _params;

	/// <summary>
	/// Constructor for parsing the result line from the streamreader
	/// </summary>
	/// <param name="line"></param>
	public ServerCommand(String line)
	{
		_line = line;
		// always 3 characters command
		_cmdID = line.substring(0, 3);
		if (!(_cmdID == "QNG"))
		{
			_params = line.substring(4).split(" ");
		}
	}

	/// <summary>
	/// This constructor makes a valid object
	/// but the command name idicates that there was something wrong
	/// </summary>
	public ServerCommand()
	{
		_line = "";
		_cmdID = "ERROR";
	}

	public String CommandName()
	{
		return _cmdID;
	}

	public String Param(int index)
	{
		return _params[index];
	}

	public int ParamCount()
	{
		return _params.length;
	}

	public String Line()
	{
		return _line; 
	}
}
