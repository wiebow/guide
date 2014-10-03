;GUIde 1.3a BlitzPlus export
;BP
;DEBUG

Include "guideexportinclude.bp"
AppTitle "EditWindow"
Global EditWindow=CreateWindow("EditWindow",0,0,400,400,0,3)
	Global panPanel0=CreatePanel(88,52,288,140,EditWindow,1)
		SetPanelColor panPanel0,0,128,255
		SetGadgetLayout panPanel0,1,1,1,0
	Global panPanel1=CreatePanel(88,200,288,120,EditWindow,1)
		SetPanelColor panPanel1,0,255,255
		SetGadgetLayout panPanel1,1,1,1,1
	Global panPanel2=CreatePanel(4,52,76,268,EditWindow,1)
		SetPanelColor panPanel2,0,255,0
		SetGadgetLayout panPanel2,1,0,1,1
	Global splSplit0.splitter=CreateSplitter(80,52,8,268,1,EditWindow)
	Global splSplit1.splitter=CreateSplitter(92,192,284,8,0,EditWindow)
		LinkToSplitter(splSplit1\canvas,splSplit0,1)
		LinkToSplitter(panPanel0,splSplit0,1)
		LinkToSplitter(panPanel1,splSplit0,1)
		LinkToSplitter(panPanel2,splSplit0,0)
		LinkToSplitter(panPanel0,splSplit1,2)
		LinkToSplitter(panPanel1,splSplit1,3)

;-mainloop--------------------------------------------------------------

Repeat
	id=WaitEvent()
	Select id
		Case $203									; move over canvas
			Select EventSource()
				Case splSplit0\canvas
					UpdateSplitter(splSplit0)
				Case splSplit1\canvas
					UpdateSplitter(splSplit1)
			End Select
		Case $401									; interacted with gadget
			DoGadgetAction( EventSource() )
		Case $803									; close gadget
			Exit
	End Select
Forever

FreeSplitter( splSplit0)
FreeSplitter( splSplit1)

;-gadget actions--------------------------------------------------------

Function DoGadgetAction( gadget )
	Select gadget
	End Select
End Function

