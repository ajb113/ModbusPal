/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modbuspal.script.panels;

import modbuspal.main.ModbusPalProject;
import modbuspal.script.ScriptRunner;
import modbuspal.script.ScriptRunnerPanel;

/**
 *
 * @author avincon
 */

public class BindingScriptRunnerPanel
extends ScriptRunnerPanel
{
    private final ModbusPalProject modbusPalProject;
    public BindingScriptRunnerPanel(ScriptRunner def, ModbusPalProject p)
    {
        super(def, false);
        modbusPalProject = p;
    }

    @Override
    protected void deleteScript()
    {
        ScriptRunner runner = getScriptRunner();
        System.out.println("Deleting " + runner.getClassName() + " binding..." );
        modbusPalProject.removeBindingScript( runner );
    }
}