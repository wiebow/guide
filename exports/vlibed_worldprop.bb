;GUIde 1.3 BlitzPlus export
Include "guideexportinclude.bp"
Global WORLDPROP=CreateWindow("World Properties",0,0,187,238,0,17)
	Global tfdWORLDID=CreateTextField(88,8,76,20,WORLDPROP)
		SetGadgetText tfdWORLDID,"newworld"
	CreateLabel("ID:",69,11,15,20,WORLDPROP,0)
	Global sprWORLDGRAV.spinner=CreateSpinner(88,64,60,20,WORLDPROP,0,1.0,0.0,10.0,0.1,"","")
	CreateLabel("Gravity force:",17,67,67,20,WORLDPROP,0)
	Global cobWORLDGRAVANGLE=CreateComboBox(88,88,80,20,WORLDPROP)
		AddGadgetItem cobWORLDGRAVANGLE,"Up"
		AddGadgetItem cobWORLDGRAVANGLE,"Down"
		AddGadgetItem cobWORLDGRAVANGLE,"Left"
		AddGadgetItem cobWORLDGRAVANGLE,"Right"
		AddGadgetItem cobWORLDGRAVANGLE,"To center"
		SelectGadgetItem cobWORLDGRAVANGLE,1
	CreateLabel("Gravity angle:",16,91,68,20,WORLDPROP,0)
	Global sprWORLDANGLE.spinner=CreateSpinner(88,116,60,20,WORLDPROP,1,1,0.0,1.0,1.0,""," degrees")
	CreateLabel("Angle:",53,119,31,20,WORLDPROP,0)
	Global tfdWORLDNAME=CreateTextField(88,36,76,20,WORLDPROP)
		SetGadgetText tfdWORLDNAME,"My World"
	CreateLabel("Name:",53,39,31,20,WORLDPROP,0)
	Global cobWORLDROT=CreateComboBox(88,140,80,20,WORLDPROP)
		AddGadgetItem cobWORLDROT,"Off"
		AddGadgetItem cobWORLDROT,"Slow"
		AddGadgetItem cobWORLDROT,"Medium"
		AddGadgetItem cobWORLDROT,"Hard"
		SelectGadgetItem cobWORLDROT,0
	CreateLabel("Rotation speed:",7,143,77,20,WORLDPROP,0)
	Global btnWORLDCLOSE=CreateButton("Close",100,176,64,20,WORLDPROP,1)

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
		Case tfdWORLDID
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case cobWORLDGRAVANGLE
			; insert your action for cobWORLDGRAVANGLE here

		Case tfdWORLDNAME
			If EventData() = 13 Then	; user pressed return in textfield
			EndIf

		Case cobWORLDROT
			; insert your action for cobWORLDROT here

		Case btnWORLDCLOSE	; user pressed button

	End Select
	CheckSpinners( EventSource() )

End Function

