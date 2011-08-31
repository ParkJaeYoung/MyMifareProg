/*
  Copyright(C):     Advanced Card Systems Ltd

  File :         	PassiveSample.java

  Description:     	This sample program outlines the steps on how to
                   	set an ACR122 NFC reader to its passive mode and
                   	receive data

  Author:           Daryl M. Rojas

  Date:             August 05, 2008

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

public class PassiveSample extends JFrame implements ActionListener{

	//JPCSC Variables
	int retCode;	
	
	//All variables that requires pass-by-reference calls to functions are
	//declared as 'Array of int' with length 1
	//Java does not process pass-by-ref to int-type variables, thus Array of int was used.
	
	 int [] hContext = new int[1]; 
	 int [] cchReaders = new int[1];
	 int [] hCard = new int[1];
	 int [] PrefProtocols = new int[1]; 		
	 int [] RecvLen = new int [1];
	 int SendLen = 0;
	 int [] nBytesRet = new int[1];
	 byte [] SendBuff = new byte[300];
	 byte [] RecvBuff = new byte[300];
	 byte [] szReaders = new byte[1024];
	 String data;
	
	 private JButton bPassive;
	 private JButton bClear;
	 private JButton bConnect;
	 private JButton bQuit;
	 private JButton bDisconnect;
	 private JButton bInitialize;
	 private JComboBox cbReader;
	 private JScrollPane jScrollPane1;
	 private JLabel lblSelect;
	 private JTextArea mMsg;
	 private JTextField tbData;
	 static JacspcscLoader jacs = new JacspcscLoader();
    

    public PassiveSample() {
    	
    	this.setTitle("Passive Device Sample");
    	this.setLocation(50,100);
        initComponents();
        initMenu();
    }


    @SuppressWarnings("unchecked")

    private void initComponents() {

		 setSize(600,450);
	   	 lblSelect = new JLabel();
	   	 cbReader = new JComboBox();
	   	 bInitialize = new JButton();
	   	 bConnect = new JButton();
	   	 bQuit = new JButton();
	   	 bPassive = new JButton();
	   	 jScrollPane1 = new JScrollPane();
	   	 mMsg = new JTextArea();
	   	 bClear = new JButton();
	   	 bDisconnect = new JButton();
	   	 tbData = new JTextField();
	   		
	     lblSelect.setText("Select Reader");

	     String[] rdrNameDef = {"Please select reader                   "};	
		 cbReader = new JComboBox(rdrNameDef);
		 cbReader.setSelectedIndex(0);
			
	     bInitialize.setText("Initialize");
	     bConnect.setText("Connect");

	     bPassive.setText("Set Passive Mode and Receive");

	     mMsg.setColumns(20);
	     mMsg.setRows(5);
	     jScrollPane1.setViewportView(mMsg);

	        bClear.setText("Clear");

	        bDisconnect.setText("Disconnect");

	        bQuit.setText("Quit");

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(lblSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addComponent(cbReader, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                                .addComponent(bConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(bPassive, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(bInitialize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE))
	                    .addComponent(tbData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                            .addComponent(bQuit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
	                            .addComponent(bDisconnect, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
	                            .addComponent(bClear, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
	                .addContainerGap())
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                            .addComponent(lblSelect)
	                            .addComponent(cbReader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(bInitialize)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(bConnect)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(bPassive)
	                        .addGap(18, 18, 18)
	                        .addComponent(tbData, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(18, 18, 18)
	                        .addComponent(bClear)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(bDisconnect)
	                        .addGap(18, 18, 18)
	                        .addComponent(bQuit)))
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );
	        
	        bInitialize.addActionListener(this);
	        bConnect.addActionListener(this);
	        bPassive.addActionListener(this);
	        bClear.addActionListener(this);
	        bDisconnect.addActionListener(this);
	        bQuit.addActionListener(this);
        
    }

    public void actionPerformed(ActionEvent e) {
    	
		 if(bInitialize == e.getSource())
			{
				
				//1. Establish context and obtain hContext handle
				retCode = jacs.jSCardEstablishContext(ACSModule.SCARD_SCOPE_USER, 0, 0, hContext);
			    
				if (retCode != ACSModule.SCARD_S_SUCCESS)
			    {
			    
					mMsg.append("Calling SCardEstablishContext...FAILED\n");
			      	//displayOut(1, retCode, "");
			      	
			    }
				
				//2. List PC/SC card readers installed in the system
				retCode = jacs.jSCardListReaders(hContext, 0, szReaders, cchReaders);

				int offset = 0;
				cbReader.removeAllItems();
				
				for (int i = 0; i < cchReaders[0]-1; i++)
				{
					
				  	if (szReaders[i] == 0x00)
				  	{			  		
				  		
				  		cbReader.addItem(new String(szReaders, offset, i - offset));
				  		offset = i+1;
				  		
				  	}
				}
				
				if (cbReader.getItemCount() == 0)
				{
				
					cbReader.addItem("No PC/SC reader detected");
					
				}
				
				bConnect.setEnabled(true);
				bInitialize.setEnabled(false);
				bDisconnect.setEnabled(true);
			}
			

		
		 if(bConnect == e.getSource())
		 {
								
				String rdrcon = (String)cbReader.getSelectedItem();  	      	      	
			    
			    retCode = jacs.jSCardConnect(hContext, 
			    							rdrcon, 
			    							ACSModule.SCARD_SHARE_SHARED,
			    							ACSModule.SCARD_PROTOCOL_T1,
			      							hCard, 
			      							PrefProtocols);
			    
			    if (retCode != ACSModule.SCARD_S_SUCCESS)
			    {
			      	
						
			    		retCode = jacs.jSCardConnect(hContext, 
			    									rdrcon, 
			    									ACSModule.SCARD_SHARE_DIRECT,
			    									0,
			    									hCard, 
			    									PrefProtocols);
			    		
			    		if (retCode != ACSModule.SCARD_S_SUCCESS)
					    {
			    			
			    			displayOut(1, retCode, "");		    			
			    			return;
			    			
					    }
			    		else
			    		{
			    			
			    			displayOut(0, 0, "Successful connection to " + (String)cbReader.getSelectedItem());
			    			
			    		}									
			    
			    } 
			    else 
			    {	      	      
			      	
			    	displayOut(0, 0, "Successful connection to " + (String)cbReader.getSelectedItem());
			      	
			    }
			    GetFirmware();
				
			}
			
			
		 if (bClear == e.getSource())
		 {
				mMsg.setText("");
		 }
			
		 if(bQuit == e.getSource())
		 {
			 
			 this.dispose();
			 
		 }
			
		 if (bDisconnect == e.getSource())
		 {
					
			 retCode = jacs.jSCardDisconnect(hCard, ACSModule.SCARD_UNPOWER_CARD);
			    
				//release context
			 retCode = jacs.jSCardReleaseContext(hContext);
				//System.exit(0);
				
			 mMsg.setText("");
			 initMenu();
			 cbReader.removeAllItems();
			 cbReader.addItem("Please select reader ");
			 		
		 }
		 
		if (bPassive == e.getSource())
		{
			SetPassive();
		}
    }
    
	  private void GetFirmware()
      {

          // Get the firmware version of the reader
          String tmpStr;
          int index;

          ClearBuffers();
          SendBuff[0] = (byte)0xFF;
          SendBuff[1] = (byte)0x00;
          SendBuff[2] = (byte)0x48;
          SendBuff[3] = (byte)0x00;
          SendBuff[4] = (byte)0x00;
          SendLen = 5;
          RecvLen[0] = 10;

          retCode = CardControl();

      	if (retCode != ACSModule.SCARD_S_SUCCESS)		
          {

              return;

          }


          // Interpret firmware data
          tmpStr = "Firmware Version: ";

          for (index = 0; index <= RecvLen[0]; index++)
          {
             
            	  tmpStr = tmpStr + (char)(RecvBuff[index]);

          }

          displayOut(3, 0, tmpStr);
      }


	
		 public void SetPassive()
	     {

	         //Setup passive mode
	         ClearBuffers();
	         SendBuff[0] = (byte)0xFF;
	         SendBuff[1] = (byte)0x00;
	         SendBuff[2] = (byte)0x00;
	         SendBuff[3] = (byte)0x00;
	         SendBuff[4] = (byte)0x27;
	         SendBuff[5] = (byte)0xD4;
	         SendBuff[6] = (byte)0x8C;
	         SendBuff[7] = (byte)0x00;
	         SendBuff[8] = (byte)0x08;
	         SendBuff[9] = (byte)0x00;
	         SendBuff[10] = (byte)0x12;
	         SendBuff[11] = (byte)0x34;
	         SendBuff[12] = (byte)0x56;
	         SendBuff[13] = (byte)0x40;
	         SendBuff[14] = (byte)0x01;
	         SendBuff[15] = (byte)0xFE;
	         SendBuff[16] = (byte)0xA2;
	         SendBuff[17] = (byte)0xA3;
	         SendBuff[18] = (byte)0xA4;
	         SendBuff[19] = (byte)0xA5;
	         SendBuff[20] = (byte)0xA6;
	         SendBuff[21] = (byte)0xA7;
	         SendBuff[22] = (byte)0xC0;
	         SendBuff[23] = (byte)0xC1;
	         SendBuff[24] = (byte)0xC2;
	         SendBuff[25] = (byte)0xC3;
	         SendBuff[26] = (byte)0xC4;
	         SendBuff[27] = (byte)0xC5;
	         SendBuff[28] = (byte)0xC6;
	         SendBuff[29] = (byte)0xC7;
	         SendBuff[30] = (byte)0xFF;
	         SendBuff[31] = (byte)0xFF;
	         SendBuff[32] = (byte)0xAA;
	         SendBuff[33] = (byte)0x99;
	         SendBuff[34] = (byte)0x88;
	         SendBuff[35] = (byte)0x77;
	         SendBuff[36] = (byte)0x66;
	         SendBuff[37] = (byte)0x55;
	         SendBuff[38] = (byte)0x44;
	         SendBuff[39] = (byte)0x33;
	         SendBuff[40] = (byte)0x22;
	         SendBuff[41] = (byte)0x11;
	         SendBuff[42] = (byte)0x00;
	         SendBuff[43] = (byte)0x00;

	         SendLen = 44;
	         RecvLen[0] = 22;

	         retCode = CardControl();
	         if (retCode != ACSModule.SCARD_S_SUCCESS)
	         {
	             return; 
	         }

	         RecvData();

	     }

		 
		 public void RecvData()
	     {

	         byte datalen = 0;
	         int index = 0;

	         data = "";

	         //Receive first the length of
	         //the actual data to be received
	         ClearBuffers();
	         SendBuff[0] = (byte)0xFF;
	         SendBuff[1] = (byte)0x00;
	         SendBuff[2] = (byte)0x00;
	         SendBuff[3] = (byte)0x00;
	         SendBuff[4] = (byte)0x02;
	         SendBuff[5] = (byte)0xD4;
	         SendBuff[6] = (byte)0x86;

	         SendLen = 7;
	         RecvLen[0] = 6;

	         retCode = CardControl();
	         
			 if (retCode != ACSModule.SCARD_S_SUCCESS)
	         {
	             return;
	         }

	         datalen = RecvBuff[3];

	         //Send a response with a value of 90 00
	         //to the sending device
	         ClearBuffers();
	         SendBuff[0] = (byte)0xFF;
	         SendBuff[1] = (byte)0x00;
	         SendBuff[2] = (byte)0x00;
	         SendBuff[3] = (byte)0x00;
	         SendBuff[4] = (byte)0x04;
	         SendBuff[5] = (byte)0xD4;
	         SendBuff[6] = (byte)0x8E;
	         SendBuff[7] = (byte)0x90;
	         SendBuff[8] = (byte)0x00;

	         SendLen = 9;
	         RecvLen[0] = 5;

	         retCode = CardControl();
	         if (retCode != ACSModule.SCARD_S_SUCCESS)
	         {
	             return;
	         }

	         //We receive the actual data
	         ClearBuffers();
	         SendBuff[0] = (byte)0xFF;
	         SendBuff[1] = (byte)0x00;
	         SendBuff[2] = (byte)0x00;
	         SendBuff[3] = (byte)0x00;
	         SendBuff[4] = (byte)0x02;
	         SendBuff[5] = (byte)0xD4;
	         SendBuff[6] = (byte)0x86;

	         SendLen = 7;
	         RecvLen[0] = (datalen) + 5;

	         retCode = CardControl();
	         if (retCode != ACSModule.SCARD_S_SUCCESS)
	         {
	             return;
	         }

	         for (index = 3; index < RecvLen[0] - 2; index++)
	         {
	            
	             data = data + (char)(RecvBuff[index]);        

	         }

	         //We send the response with a value of 90 00
	         //to the sending device
	         ClearBuffers();
	         SendBuff[0] = (byte)0xFF;
	         SendBuff[1] = (byte)0x00;
	         SendBuff[2] = (byte)0x00;
	         SendBuff[3] = (byte)0x00;
	         SendBuff[4] = (byte)0x04;
	         SendBuff[5] = (byte)0xD4;
	         SendBuff[6] = (byte)0x8E;
	         SendBuff[7] = (byte)0x90;
	         SendBuff[8] = (byte)0x00;

	         SendLen = 9;
	         RecvLen[0] = 5;

	         retCode = CardControl();
	         if (retCode != ACSModule.SCARD_S_SUCCESS)
	         {
	             return;
	         }

	         tbData.setText(data);

	     }
  


	  public int CardControl()
      {

          String tempstr = "";
          int index = 0;

          for (index = 0; index < SendLen; index++)
          {
               tempstr = tempstr + " " +  Integer.toHexString(((Byte)SendBuff[index]).intValue() & 0xFF).toUpperCase();
  			
          }

          displayOut(2,0,tempstr);
          
          retCode = jacs.jSCardControl(hCard, (int)ACSModule.IOCTL_CCID_ESCAPE_SCARD_CTL_CODE, SendBuff, SendLen, RecvBuff, RecvLen, nBytesRet);

          if (retCode != ACSModule.SCARD_S_SUCCESS)	          
          {
              
              displayOut(2, retCode, "");
              return retCode;
          }

          tempstr = "";

          for (index = 0; index < RecvLen[0]; index++)
          {
              tempstr = tempstr + " " + Integer.toHexString(((Byte)RecvBuff[index]).intValue() & 0xFF).toUpperCase();
          }
   
          displayOut(3, 0, tempstr);

          return retCode;

      }
	
	public void displayOut(int mType, int msgCode, String printText)
	{

		switch(mType)
		{
		
			case 1: 
				{
					
					mMsg.append("! " + printText);
					mMsg.append(ACSModule.GetScardErrMsg(msgCode) + "\n");
					break;
					
				}
			case 2: mMsg.append("< " + printText + "\n");break;
			case 3: mMsg.append("> " + printText + "\n");break;
			default: mMsg.append("- " + printText + "\n");
		
		}
		
	}

	
	public void ClearBuffers()
	{
		
		for(int i=0; i<262; i++)
		{
			
			SendBuff[i]=(byte) 0x00;
			RecvBuff[i]= (byte) 0x00;
			
		}
		
	}
	

	
	public void initMenu()
	{
	
		 bConnect.setEnabled(false);	
		 bDisconnect.setEnabled(false);
		 bInitialize.setEnabled(true);
		 displayOut(0, 0, "Program Ready");
		
	}
	

	
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PassiveSample().setVisible(true);
            }
        });
    }



}


