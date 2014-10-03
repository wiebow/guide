;
; blitzmax import / export include for GUIde
; version: 
;

;
;used to create the menu id consts in the export
Global menuconst

;;; <summary>Starts the BlitzMAX Export</summary>
;;; <param name="stream">stream to write to.</param>
;;; <remarks></remarks>
;;; <returns>Nothing</returns>
;;; <subsystem>GUIDE.Export</subsystem>
;;; <example>StartBlitzMAXExport( mystream )</example>
Function StartBlitzMAXExport( stream )

	WriteLine stream, "'" +APP$+ " BlitzMAX export"
	WriteLine stream, "SuperStrict"
	
	gt.gtype = editor\rootgt
	WriteLine stream, "AppTitle =" +Chr$(34)+ gt\label$ +Chr$(34)
	;
	;hack to force windows in tool mode down by 24 pixels. yadd gets added to the CreateWindow ypos string
	Local yadd = 0	
	If gt\tool = True Then yadd = 24
	
	;
	;assign variables here here
	;rootgt first
	WriteLine stream, "Global " + gt\name$ + ":TGadget"
	;
	;do rest
	ExportGadgetDeclarationsMAX(gt\childlist, stream)
	menuconst = 101
	ExportMenuDeclarationsMAX(editor\rootmt\childlist, stream)
	
	; create code for rootgt
	Local style$ = DetermineMAXStyle(gt)	
	code$ = gt\name$ + "=CreateWindow("+Chr$(34)+gt\label$+Chr$(34)+","+Int(gt\xpos)+","+Int(gt\ypos)+yadd
	code$ = code$ + ","+ GadgetWidth(EDITWINDOW)+","+ GadgetHeight(EDITWINDOW)+",Desktop()," + style +")"

	; write rootgt code to file
	WriteLine stream, code$

	; do children
	CreateBlitzMAXCode(gt\childlist, stream)

	;
	;do menus
	If editor\rootgt\menu = True
		WriteLine stream, ""
		WriteLine stream, "'-menus-----------------------------------------------------------------"
		WriteLine stream, ""
		CreateBlitzMAXMenus(editor\rootmt\childlist, stream)
		WriteLine stream, "UpdateWindowMenu " + editor\rootgt\name$
	EndIf

	; make loop
	If editor\exportloop
		CreateBlitzMAXLoop(stream)
	Else
		WriteLine stream, "While WaitEvent()<>EVENT_WINDOWCLOSE"
		WriteLine stream, "Wend"
	EndIf
End Function

;;; <summary>Exports the menus to code</summary>
;;; <param name="ml">linked list of menus</param>
;;; <param name="stream">stream to write to</param>
;;; <remarks></remarks>
;;; <returns>Nothing</returns>
;;; <subsystem>GUIDE.Export</subsystem>
;;; <example></example>
Function CreateBlitzMAXMenus(ml.mlist, stream)
	;
	; creates code for menu items in passed list

	Local code$, txt$
	;
	; create menu items
	While ml <> Null
		mt.mtype = ml\mt
		
		If mt\parent = editor\rootmt
			;
			;if gadget is child of rootmt, create menu named after label
			code$ = Lower(mt\label$)+ "menu=CreateMenu(" +Chr$(34)+ mt\label$+Chr$(34) + ",0,WindowMenu("+editor\rootgt\name$+"))"
			WriteLine stream, code$
		Else
			If mt\checkable = True Or mt\active = False Or mt\childlist <> Null
				code$ = Lower(mt\label$)+"menu="				
			EndIf
			code$ = code$ + "CreateMenu(" +Chr$(34)+ mt\label$ + Chr$(34) + ",MENU_" + Upper(mt\label$)+","+mt\parent\label$+"menu"

			If mt\shortcut$ <> ""
				txt$ = ",KEY_"+Upper(mt\shortcut$)
				Select mt\shortcutmod
					Case 0
						txt$ = txt$ + ", MODIFIER_SHIFT)"
					Case 1
						txt$ = txt$ + ", MODIFIER_COMMAND)"
					Case 2
	;					txt$ = txt$ + ", MODIFIER_SHIFT)"
	;					txt$ = Chr$(8) + "ALT+" + mt\shortcut$
				End Select
			EndIf

			code$ = code$ + txt$+")"
			WriteLine stream, code$			
			
			If mt\active = False Then WriteLine stream, "DisableMenu " + Lower(mt\label$)+"menu"
			If mt\checked = True Then WriteLine stream, "CheckMenu " + Lower(mt\label$)+"menu"
		EndIf		
		;
		; do possible children
		If mt\childlist <> Null Then CreateBlitzMAXMenus(mt\childlist, stream)
		
		ml = ml\nxt
	Wend

End Function

Function CreateBlitzMAXCode( l.list, stream )
	;
	; creates a line of code for each gadget , and writes it to the passed stream

	Local style, indent$, layoutflags, custom, childlist

	layoutflags = False
	If editor\indentedcode								; then set indent level and string
		editor\indentlevel = editor\indentlevel+1
		indent$ = String$( Chr$(9), editor\indentlevel )
	Else
		indent$ = ""
	EndIf

	While l <> Null
		gt.gtype = l\gt
		style = DetermineStyle( gt )

		; export layoutflags if window can be resized
		If editor\rootgt\resize Then layoutflags = True

		; make children orient towards parent, not to editcanvas
		xpos = Int(gt\xpos - gt\parent\xpos)
		ypos = Int(gt\ypos - gt\parent\ypos)
		
		; make groupbox children align properly (client coordinates are tighter to the border than the custom
		; blitplus groupbox... nasty hack this.
		If gt\parent\version = GROUPBOX
			xpos=xpos-4
			ypos=ypos-16
		EndIf
		
		; more hacks
		If gt\parent\version = TABPANEL
			xpos=xpos-2
			ypos=ypos+2
		EndIf

		parent$ = gt\parent\name$
		If gt\parent\version = FOLDBOX1 Then parent$ = parent$ + "\opencanvas"

		; determine gadget type
		custom = False				; set if gadget is a custom gadget, so no layoutflags are set
		childlist = True			; set if gadget is not to be exported, and childlist neither

		Select gt\version
			Case SPLITTER
				custom = True
;				childlist = False
			Case IMAGE
				custom = True
;				code$ = indent$ + editor\prefix$ + gt\name$+":TGadget=CreateImageBox(" + xpos +","+ ypos +","+ Chr$(34)+ StripPath$( gt\image$ ) + Chr$(34) + "," +parent+ ")"
;				WriteLine stream, code$
			Case COLSELECTOR
				custom = True
;				code$ = indent$ + editor\prefix$ + gt\name$+".CSelector=CreateColorSelector(" + xpos +","+ ypos +","+ gt\width+ "," +gt\height+ "," + gt\r + "," + gt\g + "," + gt\b + "," + parent + ")"
;				WriteLine stream, code$
;				code$=indent$+ "	SetGadgetLayout "+gt\name$+"\canvas,"+gt\layoutleft+","+gt\layoutright+","+gt\layouttop+","+gt\layoutbottom
;				WriteLine stream, code$
			Case FOLDBOX1
				custom = True
;				code$ = indent$ + editor\prefix$ + gt\name$+".foldbox=CreateFoldbox("+Chr$(34)+ gt\label$ +Chr$(34)+ "," +ypos+ ","+gt\height+","+gt\parent\name$+ ")"
;				WriteLine stream, code$
;				custom = True		; don't do these settings later on
			Case SPINNER
				custom = True			
;				code$ = indent$ + editor\prefix$ + gt\name$+".spinner=CreateSpinner(" +xpos+ "," +ypos+ "," +gt\width+ "," +gt\height+ "," +parent$+ "," +gt\integer+ "," +gt\defaultstring$+ "," +gt\minval+ "," +gt\maxval+ "," +gt\stp+ "," +Chr$(34)+gt\prefix$+Chr$(34)+ "," +Chr$(34)+gt\suffix$+Chr$(34)+ ")"
;				WriteLine stream, code$
;				; we need to address the gadgets in the type, so gadgets settings are done here.
;				; set gadget layout flags
;				If layoutflags = True
;					code$=indent$+ "	SetGadgetLayout "+gt\name$+"\textgad,"+gt\layoutleft+","+gt\layoutright+","
;					code$=code$+gt\layouttop+","+gt\layoutbottom
;					WriteLine stream, code$
;				EndIf

				; disable gadget if needed
;				If gt\enable = False
;					code$ = indent$+ "	DisableGadget "+gt\name$ + "\textgad"
;					WriteLine stream, code$
;					code$ = indent$+ "	DisableGadget "+gt\name$ + "\slider"
;					WriteLine stream, code$
;				EndIf

				; do not hide if parent is hidden as well
;				If gt\hide = True And gt\parent\hide = False
;					code$ = indent$+ "	HideGadget " + gt\name$+ "\textgad"
;					WriteLine stream, code$
;					code$ = indent$+ "	HideGadget " + gt\name$+ "\slider"
;					WriteLine stream, code$
;				EndIf
			Case GROUPBOX		; done
				code$ = indent$ + gt\name$+"=CreatePanel("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+ ",PANEL_GROUP,"+Chr$(34)+ gt\label$ +Chr$(34)+")"
				WriteLine stream, code$
;				custom = True				; avoid blitzplus native gadget settings later on
			Case SEPLINE		; done
				custom = True			
;				code$ = indent$ + editor\prefix$ + gt\name$+"=CreateLine( " + Chr$(34)+ gt\label$ +Chr$(34)+ ","+xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+","+ gt\direction +")"
;				WriteLine stream, code$
			Case TREEVIEW		; done
				code$ = indent$ + gt\name$+"=CreateTreeView(" +xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$
			Case HTML			; done
				code$ = indent$ + gt\name$+"=CreateHtmlView(" +xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				If gt\defaulturl$ <> ""
					code$ = indent$ + "	HtmlViewGo " + gt\name$ +"," +Chr$(34)+ gt\defaulturl$ +Chr$(34)
					WriteLine stream, code$
				EndIf
			Case LISTBOX		; done
				code$ = indent$ + gt\name$+"=CreateListBox(" +xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				; do items
				items$ = gt\items$
				laststart = 1

				If Right$( items$, 1 ) <> ";" Then items$ = items$ + ";"
				For count = 1 To Len( items$ )
					If Mid$( items$, count, 1 ) = ";"
						lastsep = count
						txt$ = Mid$( items$, laststart, lastsep-laststart )
						laststart = count + 1
						code$ = indent$+ "	AddGadgetItem " + gt\name$ +","+Chr$(34)+txt$+Chr$(34)
						WriteLine stream, code$
					EndIf
				Next

				; set default item
				code$ = indent$+ "	SelectGadgetItem " + gt\name$ +","+ gt\defaultitem
				WriteLine stream, code$

			Case TEXTAREA		; done
				code$ = indent$ + gt\name$+"=CreateTextArea(" +xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+","+style+")"
				WriteLine stream, code$

				; set text
				code$ = indent$ + "SetGadgetText " +gt\name$+ "," +Chr$(34)+ gt\defaultstring$ +Chr$(34)
				WriteLine stream, code$

				; set backdrop color
				code$ = indent$ + "SetTextAreaColor " + gt\name$ +","+ gt\r +","+ gt\g +","+ gt\b +",1"
				WriteLine stream, code$

				; set textcolor
				code$ = indent$ + "SetTextAreaColor " + gt\name$ +","+ gt\textr +","+ gt\textg +","+ gt\textb +",0"
				WriteLine stream, code$

			Case BUTTON			; done
				code$ = indent$ + gt\name$+"=CreateButton("+Chr$(34)+gt\label$+Chr$(34)+","+xpos+","+ypos+"," + gt\width+","+gt\height+","+parent$+",BUTTON_PUSH)"
				WriteLine stream, code$

			Case LABEL			; done

				; generate handle if needed
				If editor\labelhandles
					prefix$ =  gt\name$ + "="
				Else
					; never create layoutflags when there is no handle exported
					layoutflags = False
				EndIf

				code$ = indent$ + prefix$ +"CreateLabel("+Chr$(34)+gt\label$+Chr$(34)+","+xpos+","+ypos+"," + gt\width+","+gt\height+","+parent$+","+style+")"
				WriteLine stream, code$

			Case TEXTFIELD		; done
				code$ = indent$ + gt\name$+"=CreateTextField("+xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				; set default text, if needed
				If gt\defaultstring$ <> ""
					code$ = indent$+ "SetGadgetText " + gt\name$ +","+Chr$(34)+ gt\defaultstring +Chr$(34)
					WriteLine stream, code$
				EndIf

			Case CHECKBOX		; done
				code$ = indent$ + gt\name$+"=CreateButton("+Chr$(34)+gt\label$+Chr$(34)+","+xpos+","+ypos+","
				code$ = code$ + gt\width+","+gt\height+","+parent$+",BUTTON_CHECKBOX)"
				WriteLine stream, code$

				;turn on or off
				If gt\checked
					code$ = indent$+ "SetButtonState " + gt\name$ +","+ gt\enable
					WriteLine stream, code$
				EndIf

			Case RADIOBUTTON	; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateButton("+Chr$(34)+gt\label$+Chr$(34)+","+xpos+","+ypos+"," + gt\width+","+gt\height+","+parent$+",BUTTON_RADIO)"
				WriteLine stream, code$

				; turn on or off
				If gt\checked
					code$ = indent$+ "SetButtonState " + gt\name$ +","+gt\enable
					WriteLine stream, code$
				EndIf

			Case COMBOBOX		; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateComboBox("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				; add items

				items$ = gt\items$
				laststart = 1

				If Right$( items$, 1 ) <> ";" Then items$ = items$ + ";"

				For count = 1 To Len( items$ )

					; find each item and write code for it
					If Mid$( items$, count, 1 ) = ";"
						lastsep = count
						txt$ = Mid$( items$, laststart, lastsep-laststart )
						laststart = count + 1

						code$ = indent$+ "AddGadgetItem "+gt\name$+","+Chr$(34)+txt$+Chr$(34)
						WriteLine stream, code$
					EndIf
				Next

				; set default item
				code$ = indent$+ "SelectGadgetItem " + gt\name$ +","+ gt\defaultitem
				WriteLine stream, code$

			Case TABBER			; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateTabber("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				; add items
				items$ = gt\items$
				laststart = 1
				itemcount = 0

				; make sure there is a ; at the end of the string.
				If Right$(items$, 1) <> ";" Then items$ = items$ + ";"

				; walk through item string to find the first ;
				For count = 1 To Len( items$ )
					If Mid$( items$, count, 1 ) = ";"
						lastsep = count
						txt$ = Mid$(items$, laststart, lastsep-laststart)
						laststart = count + 1
						;
						; default item yes or no
						If itemcount = gt\defaultitem
							sel$ = ", True"
						Else
							sel$ = ", False"
						EndIf

						code$ = indent$+ "AddGadgetItem "+gt\name$+","+Chr$(34)+txt$+Chr$(34)+ sel$
						WriteLine stream, code$
						itemcount = itemcount + 1
					EndIf
				Next

			Case PROGRESSBAR	; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateProgBar("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				; set default value
				If gt\progressval > 0
					code$ = indent$+ "UpdateProgBar " + gt\name$ +","+ gt\progressval#
					WriteLine stream, code$
				EndIf

			Case CANVAS			; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateCanvas("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				If gt\pointershow = False
					code$ = indent$+ "HidePointer " + gt\name$
					WriteLine stream, code$
				EndIf

				; is this canvas oversized? if yes, create slider code alongside its parent

				CheckOversizedChild( gt, indent$, stream )

			Case PANEL			; done

				; create gadget
				code$ = indent$ + gt\name$+"=CreatePanel("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+","+style+")"
				WriteLine stream, code$

				; backdrop color, but only if different from system backdrop color

				If gt\r <> editor\gadr
					code$ = indent$+ "SetPanelColor "+gt\name$ +","+ gt\r +","+ gt\g +","+ gt\b
					WriteLine stream, code$
				EndIf

				; backdrop image?

				If gt\image$ <> ""
					code$ = indent$+ "SetPanelImage " + gt\name$ +","+ Chr$(34)+ gt\image$ +Chr$(34)
					WriteLine stream, code$
				EndIf

				; is this panel oversized? if yes, create slider code alongside its parent

				CheckOversizedChild( gt, indent$, stream )

			Case TABPANEL

				; if tabpanel is out of range, then don't export it
				If gt\tabitem >= 0
					code$ = indent$ + gt\name$+"=CreatePanel(0,0,ClientWidth("+gt\parent\name$+"),ClientHeight("+gt\parent\name$+"),"+parent$+","+style+")"
					WriteLine stream, code$

					; hide if not active tab panel
					If gt\parent\defaultitem <> gt\tabitem
						WriteLine stream, indent$ + "HideGadget " + gt\name$
					EndIf
				Else
					custom = True		; don't do layout flags, etc
					childlist = False	; don't do childlist either
				EndIf

			Case SLIDER			; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateSlider("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+","+style+")"
				WriteLine stream, code$

		End Select

		; export label for gadget if needed

		If gt\autolabel$ <> ""
			width = StringWidth(gt\autolabel$)
			height = 20
			xpos = xpos - 4 - width
			ypos = ypos + 3
			;If gt\version = COMBOBOX Then ypos = ypos + 2
			code$ = indent$ + prefix$ +"CreateLabel("+Chr$(34)+gt\autolabel$+Chr$(34)+","+xpos+","+ypos+"," + width+","+height+","+parent$+","+style+")"
			WriteLine stream, code$
		EndIf

		; do settings which are needed for every gadget, except custom gadgets

		If custom = False

			; set gadget layout flags
			If layoutflags = True
				code$=indent$+ "SetGadgetLayout "+gt\name$+","+gt\layoutleft+","+gt\layoutright+","
				code$=code$+gt\layouttop+","+gt\layoutbottom
				WriteLine stream, code$
			EndIf

			; disable gadget if needed
			If gt\enable = False
				code$ = indent$+ "DisableGadget "+gt\name$
				WriteLine stream, code$
			EndIf

			; do not hide if parent is hidden as well
			If gt\hide = True And gt\parent\hide = False
				code$ = indent$+ "HideGadget " + gt\name$
				WriteLine stream, code$
			EndIf
		EndIf

		; do possible children, unless childlist flag has been cleared (tabpanel)
		If l\gt\childlist <> Null And childlist = True Then CreateBlitzMAXCode(l\gt\childlist, stream)

		; go to next
		l = l\nxt
	Wend

	; go back one indention level
	editor\indentlevel = editor\indentlevel-1

End Function

Function CreateBlitzMAXLoop( stream )

	; creates code for a mainloop and event handling for the gadgets in this project

	; create main loop

	WriteLine stream, ""
	WriteLine stream, "'-mainloop--------------------------------------------------------------"
	WriteLine stream, ""
	WriteLine stream, "Repeat"
	WriteLine stream, "	Select WaitEvent()"
	WriteLine stream, "		Case EVENT_GADGETACTION						' interacted with gadget"
	WriteLine stream, "			DoGadgetAction()"
	WriteLine stream, "		Case EVENT_WINDOWCLOSE						' close gadget"
	WriteLine stream, "			Exit"
	If editor\rootgt\menu = True
		WriteLine stream, "		Case EVENT_MENUACTION					' selected a menu"
		WriteLine stream, "			DoMenuAction()"
	EndIf
	WriteLine stream, "	End Select"
	WriteLine stream, "Forever"
	WriteLine stream, ""

	; main loop done.
	; create clean up code after exiting loop

	WriteLine stream, "'-gadget actions--------------------------------------------------------"
	WriteLine stream, ""
	WriteLine stream, "Function DoGadgetAction()"
	WriteLine stream, "	Select EventSource()"

	; add all gadgets which apply for an action here.
	; insert standard action for tabbers, spinners, etc
	For gt.gtype = Each gtype
		Select gt\version
			Case PANEL,CANVAS

				; a panel or canvas. normaly, no action is required, but if these are oversized
				; then we need to adress the sliderbars.

				If gt\parent\largechildx

					; get slider name

					slname$ = gt\parent\name$ + "SliderH"

					; there could be a vertical slider as well, so lets make a choice here

					If gt\parent\largechildy

						; get vertical slidername as well

						slvname$ = gt\parent\name$ + "SliderV"
						code$ = "		Case "+slname$+","+slvname$
						WriteLine stream, code$
						code$="			SetGadgetShape " + gt\name$+",-SliderValue("+slname$+"),"+"-SliderValue("+slvname$+"),GadgetWidth("+gt\name$+"),GadgetHeight("+gt\name$+")"
					Else
						code$ = "		Case "+slname$
						WriteLine stream, code$
						code$="			SetGadgetShape " + gt\name$+",-SliderValue("+slname$+"),0,GadgetWidth("+gt\name$+"),GadgetHeight("+gt\name$+")"
					EndIf

					WriteLine stream, code$
					WriteLine stream, ""

				ElseIf gt\parent\largechildy

					; get slider name

					slvname$ = gt\parent\name$ + "SliderV"

					code$ = "		Case " + slvname$
					WriteLine stream, code$
					code$="			SetGadgetShape " + gt\name$+",0,-SliderValue("+slvname$+"),GadgetWidth("+gt\name$+"),GadgetHeight("+gt\name$+")"
					WriteLine stream, code$
					WriteLine stream, ""
				EndIf

			Case BUTTON
				WriteLine stream, "		Case " + gt\name$ + "	' user pressed button"
				WriteLine stream, ""

			Case CHECKBOX
				WriteLine stream, "		Case " + gt\name$ + "	' user changed checkbox"

				; see if checkbox is in a group. enable or disable gadgets belonging to same group
				; depending on button state

				If gt\group$ <> ""

					WriteLine stream, "			If ButtonState(" + gt\name$ + ") = True"

					; enable gadgets in same group

					For gt1.gtype = Each gtype
						If gt1\group$ = gt\group$ And gt1 <> gt

							If gt1\version = SPINNER
								WriteLine stream, "				EnableSpinner( " + gt1\name$ + " )"
							Else
								WriteLine stream, "				EnableGadget " + gt1\name$
							EndIf

						EndIf
					Next

					WriteLine stream, "			Else"

					; disable gadgets in same group

					For gt1.gtype = Each gtype
						If gt1\group$ = gt\group$ And gt1 <> gt

							If gt1\version = SPINNER
								WriteLine stream, "				DisableSpinner( " + gt1\name$ + " )"
							Else
								WriteLine stream, "				DisableGadget " + gt1\name$
							EndIf
						EndIf
					Next

					WriteLine stream, "			EndIf"

				EndIf

				WriteLine stream, ""

			Case RADIOBUTTON
				WriteLine stream, "		Case " + gt\name$ + "	' user selected radiobox"

				; disable other radionbuttons in the same group

				If gt\group$ <> ""
					For gt1.gtype = Each gtype
						If gt1\group$ = gt\group$ And gt1 <> gt
							WriteLine stream, "			SetButtonState " + gt1\name$ + ", False"
						EndIf
					Next
				EndIf

				WriteLine stream, ""

			Case TEXTFIELD
				WriteLine stream, "		Case " + gt\name$
				WriteLine stream, "			If EventData() = 13 Then	' user pressed return in textfield"
				WriteLine stream, "			EndIf"
				WriteLine stream, ""

			Case RADIOBUTTON
				WriteLine stream, "		Case " + gt\name$
				WriteLine stream, "			' insert your action for " + gt\name$ + " here"
				WriteLine stream, ""

			Case COMBOBOX
				WriteLine stream, "		Case " + gt\name$

				; if the group$ of this combobox gadget is not empty then there should be
				; panels corresponding to each item of the combobox.
				; these panels should be on the same parent as this combobox.

				; the panel groupname should have a number as suffix
				; if the combobox group name is 'group' then the panels should have the groupname
				; 'group0' for item 0, etc.

				If gt\group$ <> ""
					WriteLine stream, "			item = SelectedGadgetItem( " + gt\name$ + " )"
					WriteLine stream, "			Select item"

					For count = 0 To gt\itemcount
						WriteLine stream, "				Case " + Str count

						; walk through childlist of combobox parent.
						; skip current combobox :)

						l.list = gt\parent\childlist
						While l <> Null
							If l\gt <> gt
								If l\gt\group$ = gt\group$ + count
									WriteLine stream, "					ShowGadget " + l\gt\name$
								Else
									WriteLine stream, "					HideGadget " + l\gt\name$
								EndIf
							EndIf
							l = l\nxt
						Wend
					Next
					WriteLine stream, "			End Select"
				Else
					WriteLine stream, "			' insert your action for " + gt\name$ + " here"
				EndIf
				WriteLine stream, ""

			Case TREEVIEW
				WriteLine stream, "		Case " + gt\name$ + "	' user selected treeview node"
				WriteLine stream, ""

			Case TABBER
				If gt\itemcount > 1
					WriteLine stream, "		Case " + gt\name$ + "	' user changed tabber item"
					WriteLine stream, "			Select SelectedGadgetItem(" + gt\name$ + ")"
					;
					; create label logic
					For count = 0 To gt\itemcount - 1
						WriteLine stream, "				Case " + Str count
						;
						; walk through childlist of tabber, show or hide correct tab panel
						l.list = gt\childlist
						While l <> Null
							If l\gt\tabitem >= 0						; dont do out of range tabpanels
								If l\gt\tabitem = count
									WriteLine stream, "					ShowGadget " + l\gt\name$
								Else
									WriteLine stream, "					HideGadget " + l\gt\name$
								EndIf
							EndIf
							l = l\nxt
						Wend
					Next
					WriteLine stream, "			End Select"
					WriteLine stream, ""
				EndIf
	
			Case SLIDER
				WriteLine stream, "		Case " + gt\name$ + "	' user modified slider"
				WriteLine stream, ""

		End Select
	Next
	;
	; finish select statement
	WriteLine stream, "	End Select"
	WriteLine stream, "End Function"
	WriteLine stream, ""
	
	
	;
	; create DoMenuAction() function here
	If editor\rootgt\menu = True

		WriteLine stream, "'-menu actions--------------------------------------------------------"
		WriteLine stream, ""
		WriteLine stream, "Function DoMenuAction()"
		WriteLine stream, "	Select EventData()"
		;
		; loop through menu items
		For mt.mtype = Each mtype
			;
			; only export items at end of branch
			If mt\parent <> Null
				If mt\parent\version <> MENUROOT And mt\childlist = Null
					WriteLine stream, "		Case MENU_" + Upper(mt\label$)

					If mt\checkable
						Local label$ = Lower(mt\label$) + "menu"
						WriteLine stream, "			If MenuChecked(" + label$ + ") = True"
						WriteLine stream, "				UnCheckMenu " + label$
						WriteLine stream, "			Else"
						WriteLine stream, "				CheckMenu " + label$
						WriteLine stream, "			EndIf"
						WriteLine stream, "			UpdateWindowMenu " + editor\rootGT\name$
					EndIf

					WriteLine stream, "			' insert action for menu item '" + mt\label$ + "' here."
					WriteLine stream, ""
				EndIf
			EndIf
		Next

		WriteLine stream, "	End Select"
		WriteLine stream, "End Function"
		WriteLine stream, ""
	EndIf
End Function

Function ExportGadgetDeclarationsMAX(l.list, stream)
	;
	;exports gadget variables
	;calls again when gadget in list has a childlist
	
	While l <> Null
		Select l\gt\version
			Case SEPLINE, FOLDBOX1, IMAGE, COLSELECTOR, SPLITTER
				; dont export these declarations

			Default
				WriteLine(stream, "Global " + l\gt\name$ + ":TGadget")
				If l\gt\childlist <> Null Then ExportGadgetDeclarationsMAX(l\gt\childlist, stream)
		End Select
		;
		; go to next
		l = l\nxt					
	Wend
End Function

Function ExportMenuDeclarationsMAX(l.mlist, stream)
	;
	;exports menu variables
	;calls again when menu in list has a childlist
	While l <> Null
		;
		;check if menu is a child of the menu bar. if yes, create a global for it
		If l\mt\parent = editor\rootmt
			WriteLine(stream, "Global " + Lower(l\mt\label$) + "menu:TGadget")
		Else
			;
			;create const for each menu item
			WriteLine(stream, "Const MENU_" + Upper(l\mt\label$) + ":Int=" + menuconst)
			menuconst = menuconst + 1
			;
			;create a global if the menu is checkable or if it can be enabled/disabled		
			If l\mt\checkable = True Or l\mt\active = False
				WriteLine(stream, "Global " + Lower(l\mt\label$) + "menu:TGadget")		
			EndIf
		EndIf
		;
		; do children
		If l\mt\childlist <> Null Then ExportMenuDeclarationsMAX(l\mt\childlist, stream)
		;
		; go to next in this childlist
		l = l\nxt	
	Wend
	
End Function

Function DetermineMAXStyle$(gt.gtype)

	; determines gadget style parameter based on values in gtype and gadget version
	; tool window and client coordinates are not yet supported
	Local style$ = ""	
	Select gt\version
		Case WINDOW
			If gt\titlebar Then style$ = "WINDOW_TITLEBAR"
			If gt\resize Then style = style + "|WINDOW_RESIZABLE"
			If gt\menu Then style = style + "|WINDOW_MENU"
			If gt\statusbar Then style = style + "|WINDOW_STATUS"
			If gt\tool Then style = style + "|WINDOW_TOOL"
			If gt\client Then style = style + "|WINDOW_CLIENTCOORDS"
;		Case BUTTON
;			style = 1
;		Case LABEL
;			style = gt\border
;		Case TEXTFIELD
;			style = gt\mask
;		Case TEXTAREA
;			style = gt\wordwrap
;		Case CHECKBOX
;			style = 2
;		Case RADIOBUTTON
;			style = 3
;		Case PANEL
;			style = gt\border
;		Case SLIDER
;			style = gt\direction+1

;		Case LISTBOX		; not supported
;		Case COMBOBOX		; not supported
;		Case TREEVIEW		; not supported
;		Case TABBER			; not supported
;		Case TOOLBAR		; not supported
;		Case CANVAS			; not supported
;		Case PROGRESSBAR	; not supported
	End Select
	Return style$
End Function
