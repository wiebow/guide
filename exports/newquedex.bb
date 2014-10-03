;GUIde 1.3a BlitzPlus export
;BP
Include "guideexportinclude.bp"
MouseTimer = CreateTimer(30)
AppTitle "Quedex Editor 0.1"
Global MAINWINDOW=CreateWindow("Quedex Editor 0.1",0,0,591,906,0,15)
	Global panMENU=CreatePanel(352,0,228,840,MAINWINDOW,0)
		SetGadgetLayout panMENU,0,1,1,1
		Global fbxFoldBox3.foldbox=CreateFoldbox("Plane",0,236,panMENU)
			Global grpGroupBox0=CreateGroup( "Settings",8,16,212,128,fbxFoldBox3\opencanvas)
				Global tfdPLANENAME=CreateTextField(52,20,148,20,grpGroupBox0)
					SetGadgetText tfdPLANENAME,"New Plane"
				CreateLabel("Name:",17,23,31,20,grpGroupBox0,0)
					SetGadgetLayout tfdPLANENAME,1,0,1,0
				Global sprPLANETIME.spinner=CreateSpinner(52,44,56,20,grpGroupBox0,1,60,5.0,360.0,1.0,""," secs")
					SetGadgetLayout sprPLANETIME\textgad,1,0,1,0
				CreateLabel("Time:",22,47,26,20,grpGroupBox0,0)
				Global sprPLANESET.spinner=CreateSpinner(52,68,56,20,grpGroupBox0,1,0,0.0,3.0,1.0,"","")
					SetGadgetLayout sprPLANESET\textgad,1,0,1,0
				CreateLabel("Tileset:",13,71,35,20,grpGroupBox0,0)
				Global sprPLANEJUMPS.spinner=CreateSpinner(52,92,56,20,grpGroupBox0,1,3,0.0,6.0,1.0,"","")
					SetGadgetLayout sprPLANEJUMPS\textgad,1,0,1,0
				CreateLabel("Jumps:",14,95,34,20,grpGroupBox0,0)
			Global grpGroupBox1=CreateGroup( "Dimensions",8,144,212,80,fbxFoldBox3\opencanvas)
				Global sprPLANEWIDTH.spinner=CreateSpinner(52,20,56,20,grpGroupBox1,1,20,1.0,200.0,1.0,""," tiles")
					SetGadgetLayout sprPLANEWIDTH\textgad,1,0,1,0
				CreateLabel("Width:",16,23,32,20,grpGroupBox1,0)
				Global chbPLANEXWRAP=CreateButton("Wrap X",144,20,56,16,grpGroupBox1,2)
					SetButtonState chbPLANEXWRAP,1
					SetGadgetLayout chbPLANEXWRAP,1,0,1,0
				Global sprPLANEHEIGHT.spinner=CreateSpinner(52,44,56,20,grpGroupBox1,1,20,1.0,200.0,1.0,""," tiles")
					SetGadgetLayout sprPLANEHEIGHT\textgad,1,0,1,0
				CreateLabel("Height:",13,47,35,20,grpGroupBox1,0)
				Global chbPLANEYWRAP=CreateButton("Wrap Y",144,44,56,16,grpGroupBox1,2)
					SetButtonState chbPLANEYWRAP,1
					SetGadgetLayout chbPLANEYWRAP,1,0,1,0
		Global fbxFoldBox1.foldbox=CreateFoldbox("Tile Properties",236,300,panMENU)
			Global grpGroupBox2=CreateGroup( "Type",8,16,212,180,fbxFoldBox1\opencanvas)
				Global cobTILETYPE=CreateComboBox(12,20,188,20,grpGroupBox2)
					AddGadgetItem cobTILETYPE,"Normal"
					AddGadgetItem cobTILETYPE,"Push"
					AddGadgetItem cobTILETYPE,"Hurt"
					AddGadgetItem cobTILETYPE,"Teleport"
					AddGadgetItem cobTILETYPE,"Start/Stop"
					AddGadgetItem cobTILETYPE,"Friction"
					AddGadgetItem cobTILETYPE,"Path"
					SelectGadgetItem cobTILETYPE,0
					SetGadgetLayout cobTILETYPE,1,0,1,0
				Global panTILETYPE0=CreatePanel(8,48,196,124,grpGroupBox2,0)
					SetGadgetLayout panTILETYPE0,1,1,1,1
				Global panTILETYPE1=CreatePanel(8,48,196,124,grpGroupBox2,0)
					SetGadgetLayout panTILETYPE1,1,1,1,1
					HideGadget panTILETYPE1
					Global cobPUSHDIR=CreateComboBox(96,2,96,20,panTILETYPE1)
						AddGadgetItem cobPUSHDIR,"Up"
						AddGadgetItem cobPUSHDIR,"Down"
						AddGadgetItem cobPUSHDIR,"Left"
						AddGadgetItem cobPUSHDIR,"Right"
						SelectGadgetItem cobPUSHDIR,0
					CreateLabel("Dir:",75,5,17,20,panTILETYPE1,0)
						SetGadgetLayout cobPUSHDIR,1,0,1,0
					Global chbPUSHVISIBLE=CreateButton("Visible",4,4,52,16,panTILETYPE1,2)
						SetGadgetLayout chbPUSHVISIBLE,1,0,1,0
				Global panTILETYPE2=CreatePanel(8,48,196,124,grpGroupBox2,0)
					SetGadgetLayout panTILETYPE2,1,1,1,1
					HideGadget panTILETYPE2
					Global chbHURTVISIBLE=CreateButton("Visible",4,4,96,16,panTILETYPE2,2)
						SetGadgetLayout chbHURTVISIBLE,1,0,1,0
					Global chbHURTPULSE=CreateButton("Pulse",4,24,48,16,panTILETYPE2,2)
						SetGadgetLayout chbHURTPULSE,1,0,1,0
					Global sprHURTDELAY.spinner=CreateSpinner(96,22,56,20,panTILETYPE2,0,0.5,0.0,10.0,1.0,""," secs")
						SetGadgetLayout sprHURTDELAY\textgad,1,0,1,0
					CreateLabel("Delay:",61,25,31,20,panTILETYPE2,0)
			Global grpGroupBox3=CreateGroup( "Flags",8,196,212,92,fbxFoldBox1\opencanvas)
				Global chbTILERESTRICT=CreateButton("Restricted",12,20,96,16,grpGroupBox3,2)
					SetGadgetLayout chbTILERESTRICT,1,0,1,0
				Global chbTILESPAWN=CreateButton("Spawnpoint",12,40,96,16,grpGroupBox3,2)
					SetGadgetLayout chbTILESPAWN,1,0,1,0
				Global cobTILEEVENT=CreateComboBox(104,60,96,20,grpGroupBox3)
					AddGadgetItem cobTILEEVENT,"None"
					SelectGadgetItem cobTILEEVENT,0
				CreateLabel("Trigger event:",31,63,69,20,grpGroupBox3,0)
					SetGadgetLayout cobTILEEVENT,1,0,1,0
		Global fbxFoldBox2.foldbox=CreateFoldbox("Events",536,300,panMENU)
			Global btnEVENTADD=CreateButton("Add",20,24,188,24,fbxFoldBox2\opencanvas,1)
				SetGadgetLayout btnEVENTADD,1,0,1,0
			Global grpGroupBox5=CreateGroup( "Settings",8,84,212,204,fbxFoldBox2\opencanvas)
				Global grpGroupBox6=CreateGroup( "Timer",8,120,196,72,grpGroupBox5)
					Global chbEVENTTIMER=CreateButton("Enable",12,20,60,16,grpGroupBox6,2)
						SetGadgetLayout chbEVENTTIMER,1,0,1,0
					Global sprEVENTTIME.spinner=CreateSpinner(96,20,68,20,grpGroupBox6,1,2,1.0,60.0,1.0,""," secs")
						SetGadgetLayout sprEVENTTIME\textgad,1,0,1,0
					Global chbEVENTSHOW=CreateButton("Show",96,44,80,16,grpGroupBox6,2)
						SetGadgetLayout chbEVENTSHOW,1,0,1,0
				Global tfdEVENTTEXT=CreateTextField(52,92,148,20,grpGroupBox5)
				CreateLabel("Text:",22,95,26,20,grpGroupBox5,0)
					SetGadgetLayout tfdEVENTTEXT,1,0,1,0
				Global cobEVENTTRIGGER=CreateComboBox(52,68,96,20,grpGroupBox5)
					AddGadgetItem cobEVENTTRIGGER,"None"
					SelectGadgetItem cobEVENTTRIGGER,0
				CreateLabel("Trigger:",10,71,38,20,grpGroupBox5,0)
					SetGadgetLayout cobEVENTTRIGGER,1,0,1,0
				Global tfdEVENTNAME=CreateTextField(52,20,148,20,grpGroupBox5)
					SetGadgetText tfdEVENTNAME,"New Event"
				CreateLabel("Name:",17,23,31,20,grpGroupBox5,0)
					SetGadgetLayout tfdEVENTNAME,1,0,1,0
				Global cobEVENTTYPE=CreateComboBox(52,44,96,20,grpGroupBox5)
					AddGadgetItem cobEVENTTYPE,"Show Message"
					SelectGadgetItem cobEVENTTYPE,0
				CreateLabel("Type:",20,47,28,20,grpGroupBox5,0)
					SetGadgetLayout cobEVENTTYPE,1,0,1,0
			Global cobEVENTSELECT=CreateComboBox(60,60,96,20,fbxFoldBox2\opencanvas)
				AddGadgetItem cobEVENTSELECT,"Name"
				SelectGadgetItem cobEVENTSELECT,0
			CreateLabel("Event:",24,63,32,20,fbxFoldBox2\opencanvas,0)
				SetGadgetLayout cobEVENTSELECT,1,0,1,0
			Global btnEVENTDELETE=CreateButton("Delete",164,56,44,24,fbxFoldBox2\opencanvas,1)
				SetGadgetLayout btnEVENTDELETE,1,0,1,0
		Global fbxFoldBox4.foldbox=CreateFoldbox("Image Selector",837,316,panMENU)
			Global tabTabber0=CreateTabber(8,24,208,280,fbxFoldBox4\opencanvas)
				AddGadgetItem tabTabber0,"Tiles", False
				AddGadgetItem tabTabber0,"Structures", False
				AddGadgetItem tabTabber0,"Objects", True
				SetGadgetLayout tabTabber0,1,2,1,2
				Global TabPanel0=CreatePanel(0,0,ClientWidth(tabTabber0),ClientHeight(tabTabber0),tabTabber0,0)
				HideGadget TabPanel0
					SetGadgetLayout TabPanel0,1,1,1,1
					HideGadget TabPanel0
					Global panTILEIMAGE=CreatePanel(-4,4,188,252,TabPanel0,0)
						SetGadgetLayout panTILEIMAGE,1,1,1,1
						Global cavTILEIMAGE=CreateCanvas(0,0,188,300,panTILEIMAGE)
						Global panTILEIMAGESliderV=CreateSlider(185,4,16,252,TabPanel0,2)
							SetSliderRange panTILEIMAGESliderV,ClientHeight(panTILEIMAGE),300
							SetGadgetLayout panTILEIMAGESliderV,1,1,1,1
							SetGadgetLayout cavTILEIMAGE,1,2,1,2
				Global TabPanel1=CreatePanel(0,0,ClientWidth(tabTabber0),ClientHeight(tabTabber0),tabTabber0,0)
				HideGadget TabPanel1
					SetGadgetLayout TabPanel1,1,1,1,1
					HideGadget TabPanel1
					Global panSTRUCTIMAGE=CreatePanel(0,0,188,252,TabPanel1,0)
						SetGadgetLayout panSTRUCTIMAGE,1,1,1,1
						Global cavSTRUCTIMAGE=CreateCanvas(0,0,188,300,panSTRUCTIMAGE)
						Global panSTRUCTIMAGESliderV=CreateSlider(189,0,16,252,TabPanel1,2)
							SetSliderRange panSTRUCTIMAGESliderV,ClientHeight(panSTRUCTIMAGE),300
							SetGadgetLayout panSTRUCTIMAGESliderV,1,1,1,1
							SetGadgetLayout cavSTRUCTIMAGE,1,2,1,2
				Global TabPanel2=CreatePanel(0,0,ClientWidth(tabTabber0),ClientHeight(tabTabber0),tabTabber0,0)
					SetGadgetLayout TabPanel2,1,1,1,1
					Global panOBJECTIMAGE=CreatePanel(0,0,188,252,TabPanel2,0)
						SetGadgetLayout panOBJECTIMAGE,1,1,1,1
						Global cavOBJECTIMAGE=CreateCanvas(0,0,188,300,panOBJECTIMAGE)
						Global panOBJECTIMAGESliderV=CreateSlider(189,0,16,252,TabPanel2,2)
							SetSliderRange panOBJECTIMAGESliderV,ClientHeight(panOBJECTIMAGE),300
							SetGadgetLayout panOBJECTIMAGESliderV,1,1,1,1
							SetGadgetLayout cavOBJECTIMAGE,1,2,1,2
	Global panDISPLAY=CreatePanel(0,0,348,840,MAINWINDOW,1)
		SetGadgetLayout panDISPLAY,1,1,1,1
		Global cavDISPLAY=CreateCanvas(0,0,348,840,panDISPLAY)
			SetGadgetLayout cavDISPLAY,1,1,1,1

;-menus-----------------------------------------------------------------

MENUITEM0 = WindowMenu( MAINWINDOW )
CreateMenu( "File",1,MENUITEM0 )
CreateMenu( "Edit",2,MENUITEM0 )
MENUITEM3 = CreateMenu( "View",3,MENUITEM0 )
CreateMenu( "Grid",5,MENUITEM3 )
CreateMenu( "Tile Details",6,MENUITEM3 )
MENUITEM4 = CreateMenu( "Help",4,MENUITEM0 )
CreateMenu( "Keys",7,MENUITEM4 )
CreateMenu( "About",8,MENUITEM4 )
UpdateWindowMenu MAINWINDOW

;-mainloop--------------------------------------------------------------

Repeat
	id=WaitEvent()
	Select id
		Case $201								; hit mouse button
			CheckFoldBoxes( EventSource(), EventData() )
		Case $202								; released mouse button
			GUIde\foldboxmove = False
		Case $401									; interacted with gadget
			DoGadgetAction( EventSource() )
		Case $803									; close gadget
			Exit
		Case $1001								; selected a menu
		DoMenuAction( EventData() )
		Case $4001								; timer tick
			Select EventSource()
				Case MouseTimer
					If GUIde\foldboxmove = True Then ScrollFoldBoxes()
			End Select
	End Select
Forever


;-gadget actions--------------------------------------------------------

Function DoGadgetAction( gadget )
	Select gadget
		Case tfdPLANENAME
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case chbPLANEXWRAP	; user changed checkbox

		Case chbPLANEYWRAP	; user changed checkbox

		Case cobTILETYPE
			item = SelectedGadgetItem( cobTILETYPE )
			Select item
				Case 0
					ShowGadget panTILETYPE0
					HideGadget panTILETYPE1
					HideGadget panTILETYPE2
				Case 1
					HideGadget panTILETYPE0
					ShowGadget panTILETYPE1
					HideGadget panTILETYPE2
				Case 2
					HideGadget panTILETYPE0
					HideGadget panTILETYPE1
					ShowGadget panTILETYPE2
				Case 3
					HideGadget panTILETYPE0
					HideGadget panTILETYPE1
					HideGadget panTILETYPE2
				Case 4
					HideGadget panTILETYPE0
					HideGadget panTILETYPE1
					HideGadget panTILETYPE2
				Case 5
					HideGadget panTILETYPE0
					HideGadget panTILETYPE1
					HideGadget panTILETYPE2
				Case 6
					HideGadget panTILETYPE0
					HideGadget panTILETYPE1
					HideGadget panTILETYPE2
			End Select

		Case cobPUSHDIR
			; insert your action for cobPUSHDIR here

		Case chbPUSHVISIBLE	; user changed checkbox

		Case chbHURTVISIBLE	; user changed checkbox

		Case chbHURTPULSE	; user changed checkbox

		Case chbTILERESTRICT	; user changed checkbox

		Case chbTILESPAWN	; user changed checkbox

		Case cobTILEEVENT
			; insert your action for cobTILEEVENT here

		Case btnEVENTADD	; user pressed button

		Case chbEVENTTIMER	; user changed checkbox

		Case chbEVENTSHOW	; user changed checkbox

		Case tfdEVENTTEXT
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case cobEVENTTRIGGER
			; insert your action for cobEVENTTRIGGER here

		Case tfdEVENTNAME
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case cobEVENTTYPE
			; insert your action for cobEVENTTYPE here

		Case cobEVENTSELECT
			; insert your action for cobEVENTSELECT here

		Case btnEVENTDELETE	; user pressed button

		Case tabTabber0	; user changed tabber item
			item = SelectedGadgetItem( tabTabber0 )
			Select item
				Case 0
					ShowGadget TabPanel0
					HideGadget TabPanel1
					HideGadget TabPanel2
				Case 1
					HideGadget TabPanel0
					ShowGadget TabPanel1
					HideGadget TabPanel2
				Case 2
					HideGadget TabPanel0
					HideGadget TabPanel1
					ShowGadget TabPanel2
			End Select

		Case panTILEIMAGESliderV
			SetGadgetShape cavTILEIMAGE,0,-SliderValue(panTILEIMAGESliderV),GadgetWidth(cavTILEIMAGE),GadgetHeight(cavTILEIMAGE)

		Case panSTRUCTIMAGESliderV
			SetGadgetShape cavSTRUCTIMAGE,0,-SliderValue(panSTRUCTIMAGESliderV),GadgetWidth(cavSTRUCTIMAGE),GadgetHeight(cavSTRUCTIMAGE)

		Case panOBJECTIMAGESliderV
			SetGadgetShape cavOBJECTIMAGE,0,-SliderValue(panOBJECTIMAGESliderV),GadgetWidth(cavOBJECTIMAGE),GadgetHeight(cavOBJECTIMAGE)

	End Select
	CheckSpinners( EventSource() )

End Function

;-menu actions--------------------------------------------------------

Function DoMenuAction( menuid )
	Select menuid
		Case 5

			; insert action for item 'Grid' here.

		Case 6

			; insert action for item 'Tile Details' here.

		Case 7

			; insert action for item 'Keys' here.

		Case 8

			; insert action for item 'About' here.

	End Select
End Function

