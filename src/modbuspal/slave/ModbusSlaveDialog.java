/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ModbusSlaveDialog.java
 *
 * Created on 17 déc. 2008, 11:52:45
 */

package modbuspal.slave;

import java.awt.BorderLayout;
import modbuspal.main.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import modbuspal.automation.Automation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author avincon
 */
public class ModbusSlaveDialog
extends javax.swing.JDialog
implements ModbusConst, ModbusSlaveListener
{
    private ModbusSlave modbusSlave;
    private ModbusPalGui mainGui;

    /** Creates new form ModbusSlaveDialog */
    public ModbusSlaveDialog(ModbusPalGui parent, ModbusSlave slave)
    {
        super(parent, false);
        mainGui = parent;
        setTitle( String.valueOf(slave.getSlaveId()) + ":" + slave.getName() );
        modbusSlave = slave;
        modbusSlave.addModbusSlaveListener(this);
        initComponents();
        holdingRegistersPanel.add(new ModbusRegistersPanel(mainGui, this, modbusSlave.getHoldingRegisters()),BorderLayout.CENTER);
    }

    ModbusSlave getModbusSlave()
    {
        return modbusSlave;
    }

    private void exportSlave(File exportFile, boolean withBindings, boolean withAutomations)
    throws FileNotFoundException, IOException
    {
        OutputStream out = new FileOutputStream(exportFile);

        String openTag = "<modbuspal_slave>\r\n";
        out.write( openTag.getBytes() );

        // if needed, first export automations (they need to be imported first!)
        if( withAutomations == true )
        {
            String names[] = modbusSlave.getRequiredAutomations();
            for(int i=0; i<names.length; i++)
            {
                Automation automation = ModbusPal.getAutomation( names[i] );
                automation.save(out);
            }
        }
        modbusSlave.save(out,withBindings);

        String closeTag = "</modbuspal_slave>\r\n";
        out.write( closeTag.getBytes() );
        out.close();
    }

//    private void importSlave(Document doc)
//    {
//        NodeList slaveNodes = doc.getElementsByTagName("slave");
//        if( slaveNodes.getLength()==1 )
//        {
//            importSlave(slaveNodes.item(0) );
//        }
//        else
//        {
//            ImportSlaveDialog dialog = new ImportSlaveDialog(mainGui, slaveNodes);
//            dialog.setVisible(true);
//            Node data = dialog.getImport();
//            if( data != null )
//            {
//                importSlave(data);
//            }
//        }
//    }

    private void importSlave(File importFile)
    throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(importFile);

        // normalize text representation
        doc.getDocumentElement().normalize();

        // how many slaves in the file?
        NodeList slaves = doc.getElementsByTagName("slave");

        if( slaves.getLength()==1 )
        {
            // any bindings ?
            Node uniqNode = slaves.item(0);
            Collection<Node> bindings = XMLTools.findChildren(uniqNode,"binding");
            if( bindings.size()==0 )
            {
                modbusSlave.load(uniqNode);
                return;
            }
        }

        ImportSlaveDialog dialog = new ImportSlaveDialog(mainGui, doc);
        dialog.setVisible(true);

        Node importData = dialog.getImport();
        if( importData == null )
        {
            setStatus("Import cancelled by user.");
            return;
        }

        boolean importBindings = dialog.importBindings();
        boolean importAutomations = dialog.importAutomations();
        
        if( importAutomations==true )
        {
            //TODO: import automations
            ModbusPal.loadAutomations(doc);
        }

        modbusSlave.load(importData);

        if( importBindings==true )
        {
            ModbusPal.loadBindings(doc);
        }
    }

    /** This method is called getStartingAddress within the constructor getQuantity
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        holdingRegistersPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        importButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        implementationComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        holdingRegistersPanel.setPreferredSize(new java.awt.Dimension(400, 300));
        holdingRegistersPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Holding registers", holdingRegistersPanel);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        importButton.setText("Import");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });
        jPanel1.add(importButton);

        exportButton.setText("Export");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        jPanel1.add(exportButton);

        implementationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Modbus", "J-Bus" }));
        implementationComboBox.setSelectedIndex(modbusSlave.getImplementation());
        implementationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                implementationComboBoxActionPerformed(evt);
            }
        });
        jPanel1.add(implementationComboBox);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        statusLabel.setText("-");
        jPanel3.add(statusLabel);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed

        boolean exportBindings = false;
        boolean exportAutomations = false;

        // Any bindings ?
        if( modbusSlave.hasBindings()==true )
        {
            // Create option dialog
            ExportSlaveDialog optionDialog = new ExportSlaveDialog(mainGui);
            ModbusPalGui.align(this, optionDialog);
            optionDialog.setVisible(true);

            // check that the option dialog has been validated
            if( optionDialog.isOK()==false )
            {
                return;
            }

            exportBindings = optionDialog.exportBindings();
            exportAutomations = optionDialog.exportAutomations();
        }
        
        // Create dialog
        JFileChooser saveDialog = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Slave export file (*.xmps)", "xmps");
        saveDialog.setFileFilter(filter);

        // show dialog
        saveDialog.showSaveDialog(this);

        // get selected file
        File exportFile = saveDialog.getSelectedFile();

        if( exportFile == null )
        {
            setStatus("Cancelled by user.");
            return;
        }
        
        try
        {
            exportSlave(exportFile, exportBindings, exportAutomations );
            setStatus("Export completed.");
        }
        catch (Exception ex)
        {
            Logger.getLogger(ModbusSlaveDialog.class.getName()).log(Level.SEVERE, null, ex);
            setStatus("Export failed.");
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed

        // create dialog
        JFileChooser loadDialog = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Slave export file (*.xmps)", "xmps");
        loadDialog.setFileFilter(filter);

        // show dialog
        setStatus("Importing...");
        loadDialog.showOpenDialog(this);

        // get selected file
        File importFile = loadDialog.getSelectedFile();

        if( importFile == null )
        {
            setStatus("Import cancelled by user.");
            return;
        }

        try
        {
            importSlave(importFile);
        }
        catch (Exception ex)
        {
            Logger.getLogger(ModbusSlaveDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        setStatus("Data imported.");
    }//GEN-LAST:event_importButtonActionPerformed

    private void implementationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_implementationComboBoxActionPerformed

        switch( implementationComboBox.getSelectedIndex() )
        {
            default:
            case 0: // modbus
                modbusSlave.setImplementation(IMPLEMENTATION_MODBUS);
                break;
            case 1: // J-Bus
                modbusSlave.setImplementation(IMPLEMENTATION_JBUS);
                break;
        }        
}//GEN-LAST:event_implementationComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exportButton;
    private javax.swing.JPanel holdingRegistersPanel;
    private javax.swing.JComboBox implementationComboBox;
    private javax.swing.JButton importButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

    void setStatus(String text)
    {
        statusLabel.setText(text);
    }

    @Override
    public void modbusSlaveEnabled(ModbusSlave slave, boolean enabled)
    {
    }

    @Override
    public void modbusSlaveNameChanged(ModbusSlave slave, String newName)
    {
    }

    @Override
    public void modbusSlaveImplChanged(ModbusSlave slave, int impl)
    {
        switch( impl )
        {
            default:
            case IMPLEMENTATION_MODBUS:
                implementationComboBox.setSelectedIndex(0);
                break;
            case IMPLEMENTATION_JBUS:
                implementationComboBox.setSelectedIndex(1);
                break;
        }
    }

}