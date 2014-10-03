;GUIde 1.3 BlitzPlus export
Include "guideexportinclude.bp"
Global OBJPROP=CreateWindow("Object Properties",0,0,228,318,0,49)
	Global btnClose=CreateButton("Close",148,264,64,20,OBJPROP,1)
	Global grpGroupBox1=CreateGroup( "XML Tags",4,0,212,196,OBJPROP)
		Global tfdID=CreateTextField(32,20,64,20,grpGroupBox1)
		CreateLabel("ID:",13,23,15,20,grpGroupBox1,0)
		Global tfdKey1=CreateTextField(32,44,64,20,grpGroupBox1)
		CreateLabel("Key:",6,47,22,20,grpGroupBox1,0)
		Global tfdValue1=CreateTextField(140,44,64,20,grpGroupBox1)
		CreateLabel("Value:",106,47,30,20,grpGroupBox1,0)
		Global tfdKey2=CreateTextField(32,68,64,20,grpGroupBox1)
		CreateLabel("Key:",6,71,22,20,grpGroupBox1,0)
		Global tfdKey3=CreateTextField(32,92,64,20,grpGroupBox1)
		CreateLabel("Key:",6,95,22,20,grpGroupBox1,0)
		Global tfdKey4=CreateTextField(32,116,64,20,grpGroupBox1)
		CreateLabel("Key:",6,119,22,20,grpGroupBox1,0)
		Global tfdKey5=CreateTextField(32,140,64,20,grpGroupBox1)
		CreateLabel("Key:",6,143,22,20,grpGroupBox1,0)
		Global tfdKey6=CreateTextField(32,164,64,20,grpGroupBox1)
		CreateLabel("Key:",6,167,22,20,grpGroupBox1,0)
		Global tfdValue2=CreateTextField(140,68,64,20,grpGroupBox1)
		CreateLabel("Value:",106,71,30,20,grpGroupBox1,0)
		Global tfdValue3=CreateTextField(140,92,64,20,grpGroupBox1)
		CreateLabel("Value:",106,95,30,20,grpGroupBox1,0)
		Global tfdValue4=CreateTextField(140,116,64,20,grpGroupBox1)
		CreateLabel("Value:",106,119,30,20,grpGroupBox1,0)
		Global tfdValue5=CreateTextField(140,140,64,20,grpGroupBox1)
		CreateLabel("Value:",106,143,30,20,grpGroupBox1,0)
		Global tfdValue6=CreateTextField(140,164,64,20,grpGroupBox1)
		CreateLabel("Value:",106,167,30,20,grpGroupBox1,0)
	Global grpGroupBox2=CreateGroup( "Color",4,196,212,60,OBJPROP)
		Global panObjectColor=CreatePanel(20,20,172,24,grpGroupBox2,1)
			SetPanelColor panObjectColor,220,220,170

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
		Case btnClose	; user pressed button

		Case tfdID
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdKey1
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdValue1
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdKey2
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdKey3
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdKey4
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdKey5
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdKey6
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdValue2
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdValue3
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdValue4
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdValue5
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case tfdValue6
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

	End Select
End Function

