;BP
;DEBUG

;Include "guideexportinclude.bp"
AppTitle "GUIde 2.0"
Global EditWindow=CreateWindow("GUIde 2.0",0,0,729,584,0,15)
	Global panMAINPANEL=CreatePanel(104,0,392,472,EditWindow,1)
		SetGadgetLayout panMAINPANEL,1,1,1,1
	Global cobTOOLCOMBO=CreateComboBox(0,0,96,20,EditWindow)
		AddGadgetItem cobTOOLCOMBO,"BlitzPlus"
		AddGadgetItem cobTOOLCOMBO,"WinBlitz3D"
		SelectGadgetItem cobTOOLCOMBO,0
		SetGadgetLayout cobTOOLCOMBO,1,0,1,0
	Global cavTOOLCANVAS=CreateCanvas(0,24,96,400,EditWindow)
		SetGadgetLayout cavTOOLCANVAS,1,0,1,0
	Global trvGADGETS=CreateTreeView(504,0,208,176,EditWindow)
		SetGadgetLayout trvGADGETS,0,1,1,1
	Global libPROPS=CreateListBox(504,184,208,328,EditWindow)
		AddGadgetItem libPROPS,"item0"
		SelectGadgetItem libPROPS,0
		SetGadgetLayout libPROPS,0,1,0,1

;-menus-----------------------------------------------------------------

MENUITEM0 = WindowMenu( EditWindow )
CreateMenu( "File",1,MENUITEM0 )
CreateMenu( "Edit",2,MENUITEM0 )
MENUITEM3 = CreateMenu( "View",3,MENUITEM0 )
Global MENUITEM5 = CreateMenu( "Grid",5,MENUITEM3 )
MENUITEM4 = CreateMenu( "Help",4,MENUITEM0 )
CreateMenu( "About",6,MENUITEM4 )
CreateMenu( "Help",7,MENUITEM4 )
UpdateWindowMenu EditWindow

;-mainloop--------------------------------------------------------------

Repeat
	id=WaitEvent()
	Select id
		Case $103									; pressed key
			If EventData() = 27 Then Exit 			; ESC
		Case $401									; interacted with gadget
			DoGadgetAction( EventSource() )
		Case $803									; close gadget
			Exit
		Case $1001									; selected a menu
		DoMenuAction( EventData() )
	End Select
Forever

;-gadget actions--------------------------------------------------------

Function DoGadgetAction( gadget )
	Select gadget
		Case cobTOOLCOMBO
			; insert your action for cobTOOLCOMBO here

		Case trvGADGETS	; user selected treeview node

	End Select
End Function

;-menu actions--------------------------------------------------------

Function DoMenuAction( menuid )
	Select menuid
		Case 5
			If MenuChecked(MENUITEM5) = True
				UnCheckMenu MENUITEM5
			Else
				CheckMenu MENUITEM5
			EndIf
			UpdateWindowMenu EditWindow

			; insert action for item 'Grid' here.

		Case 6

			; insert action for item 'About' here.

		Case 7

			; insert action for item 'Help' here.

	End Select
End Function

