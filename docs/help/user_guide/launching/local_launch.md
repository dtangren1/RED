<html>
<head>
<link href="PLUGINS_ROOT/org.robotframework.ide.eclipse.main.plugin.doc.user/help/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<a href="RED/../../../../help/index.html">RED - Robot Editor User Guide</a> &gt; <a href="RED/../../../../help/user_guide/user_guide.html">User guide</a> &gt; <a href="RED/../../../../help/user_guide/launching.html">Launching Tests</a> &gt; 
	<h2>Locally launched tests</h2>
<p>When <b>Robot</b> launch configuration is launched, it setups a simple server inside RED instance to which 
	execution events can be send from tests. Then it automatically starts the tests choosing suites/tests which
	has to be executed from the configuration. The tests are either started using some kind of python interpreter
	or using <a href="local_launch_scripting.html">user-defined script</a>.
	</p>
<h3>Robot launch configuration</h3>
<p>Open 
	<code><a class="command" href="javascript:executeCommand('org.eclipse.debug.ui.commands.OpenRunConfigurations')">
	Run -> Run Configurations...</a></code> dialog and under <b>Robot</b> element create new configuration. The
	configuration itself is using couple of tabs:
	</p>
<ul>
<li><b>Robot</b> - where <b>project in use</b> should be specified, as well as <b>suites</b> and/or 
		<b>tests</b> to be executed; <b>tags</b> used to include or exclude tests and <b>additional</b> 
		arguments provided by user,
		</li>
<li><b>Listener</b> - where agent <b>connection</b> type and its parameters is specified,
		</li>
<li><b>Executor</b> - where <b>executable</b> for the tests are specified
		<li><b>Environment</b> - where <b>environment variables</b> for robot process can be specified (if nothing is
		specified the variables are inherited from RED/eclipse environment variables).
	</li></li></ul>
<h4>Robot tab</h4>
<img src="images/local_config_robot.png"/>
<p>At this tab following arguments can/have to be specified:
	</p>
<ul>
<li><b>project</b> - the path to project will be passed to robot as the data source to be executed,
		</li>
<li><b>test suite(s)</b> - suites can be specified by adding files or directories, those entries will
		be translated to <code>--suite</code> arguments for Robot. Additionally when file suite is added
		the tests inside it can be chosen/excluded from execution, which translates to <code>--test</code> argument entries
		in command line call,
		</li>
<li><b>only run tests with these tags</b> - test cases to be executed are filtered by tag, this setting
		translates to <code>--include</code> argument of command line call,
		</li>
<li><b>skip tests with these tags</b> - test cases can be excluded from execution based on tags,
		this setting translates to <code>--exclude</code> argument of command line call,
		</li>
<li><b>additional Robot Framework arguments</b> - additional arguments to be passed to Robot 
		can be specified here.
		</li>
</ul>
<dl class="note">
<dt>Note</dt>
<dd>Additional arguments field accepts Eclipse <a href="string_substitution.html">string variables</a>.</dd>
</dl>
<h4>Listener tab</h4>
<img src="images/local_config_listener.png"/>
<p>At this tab, one can specify how RED will setup the server for execution tracking. By default it 
	will use localhost and random free port to start the server and wait for connection, but
	other host, port and timeout can be specified. This is especially useful when used together with 
	<a href="local_launch_scripting.html">scripted launch</a>, when the script is setting up agent connection 
	in its own way.
	</p>
<h4>Executor tab</h4>
<img src="images/local_config_exec.png"/>
<p>At this page executable for Robot tests may be configured. Firstly either python interpreter defined
	by project can be used (the one defined in red.xml file of a project, or in preferences if red.xml does 
	not specify it), or interpreter taken from <code>PATH</code> environment variable. Additionally
	freely defined arguments can be passed to interpreter.
	</p>
<p>Moreover, the whole call can be passed to a script (or other executable) with additional arguments.
	The script/executable's command line parameters are the same as RED command line during normal test execution 
    (path to python interpreter with robot call, parameters for suites/testcases etc.). Such script/external executable 
    mechanism can be used to wrap Robot execution into other tools. 
	</p>
<p>For more information read <a href="local_launch_scripting.html">Local launches scripting</a> topic in this guide.
	
	<dl class="note">
<dt>Note</dt>
<dd>Additional arguments and executable file path fields accept Eclipse <a href="string_substitution.html">string variables</a>.</dd>
</dl>
<h4>Environment tab</h4>
<img src="images/local_config_env.png"/>
<p>At this tab environment variables can be specified for robot tests process. There are 3 possibilities:</p>
<ul>
<li>no variable is specified (<b>default</b>) - the robot process will be launched with variables inherited 
		from running RED/eclipse instance,</li>
<li>variable(s) specified, in append mode - the robot process will be launched with variables inherited
		from running RED/eclipse, but with specified variables appended,</li>
<li>variable(s) specified, in replace mode - the robot process will only get those variables which are
		defined in table.</li>
</ul>
<h2>Launching tests</h2>
<p>When configuration is properly set it may be launched either in debug or run mode. RED will 
	take care of setting up both server and agent as well as running the tests.
	</p>
<h2>Debugging local launches</h2>
<p>When local configuration is launched it already have agent script passed as a listener, so
	the agent is sending debug information back to RED during execution. Working with debugger is
	described in <a href="debug.html">debugging robot</a> topic.
	</p>
<br/>
<br/>
</p></body>
</html>