;GUIde 1.3 BlitzPlus export
Include "guideexportinclude.bp"
Global ADDOBJWIN=CreateWindow("Add Object",0,0,189,108,0,49)
	Global grpGroupBox0=CreateGroup( "Type",4,0,172,52,ADDOBJWIN)
		Global cobObjectType=CreateComboBox(8,20,156,20,grpGroupBox0)
			AddGadgetItem cobObjectType,"Fuel Pod"
			AddGadgetItem cobObjectType,"Reactor"
			AddGadgetItem cobObjectType,"Gun"
			AddGadgetItem cobObjectType,"Klystron Pod"
			AddGadgetItem cobObjectType,"Spawn Point"
			AddGadgetItem cobObjectType,"Check Point"
			SelectGadgetItem cobObjectType,0
	Global btnAddCancel=CreateButton("Cancel",108,56,64,20,ADDOBJWIN,1)
	Global btnAddOK=CreateButton("OK",36,56,64,20,ADDOBJWIN,1)

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
	End Select
Forever

;-gadget actions--------------------------------------------------------

Function DoGadgetAction( gadget )
	Select gadget
		Case cobObjectType
			; insert your action for cobObjectType here

		Case btnAddCancel	; user pressed button

		Case btnAddOK	; user pressed button

	End Select
End Function

