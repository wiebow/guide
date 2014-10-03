;
; blitzplus import / export include for GUIde
; version: 5/31/2004
;

;;; <summary>Starts the BlitzPlus Export</summary>
;;; <param name="stream">stream to write to.</param>
;;; <remarks></remarks>
;;; <returns>Nothing</returns>
;;; <subsystem>GUIDE.Export</subsystem>
;;; <example>StartBlitzPlusExport( "myfile.bb" )</example>
Function StartBlitzPlusExport( stream )

	WriteLine stream, ";" +APP$+ " BlitzPlus export"
	WriteLine stream, "Include " + Chr$(34)+"guideexportinclude.bb" + Chr$(34)
	If editor\foldboxcount <> 0 Then WriteLine stream, "MouseTimer = CreateTimer(30)"

	gt.gtype = editor\rootgt
	WriteLine stream, "AppTitle " +Chr$(34)+ gt\label$ +Chr$(34)
	style = DetermineStyle( gt )

	WriteLine stream, ""
	;
	;hack to force windows in tool mode down by 24 pixels
	Local yadd% = 0	
	If gt\tool = True Then yadd = 24

	;
	;assign variables here here
	;rootgt first
	WriteLine stream, "Global " + gt\name$
	;
	;do rest
	ExportGadgetDeclarationsPLUS(gt\childlist, stream)
	ExportMenuDeclarationsPLUS(editor\rootmt\childlist, stream)
	
	; create code for rootgt
	WriteLine stream, ""
	code$ = gt\name$ + "=CreateWindow("+Chr$(34)+gt\label$+Chr$(34)+","+Int(gt\xpos)+","+Int(gt\ypos)+yadd
	code$ = code$ + ","+ GadgetWidth( EDITWINDOW )+","+ GadgetHeight(EDITWINDOW)+",0," + style +")"

	; write rootgt code to file
	WriteLine stream, code$

	; do children
	CreateBlitzPlusCode( gt\childlist, stream )

	; do splitter export. these gadgets MUST be created last
	CreateSplitterCode( stream )

	; do menus
	If editor\rootgt\menu = True
		WriteLine stream, ""
		WriteLine stream, ";-menus-----------------------------------------------------------------"
		WriteLine stream, ""

		; create code for menu root
		WriteLine stream, "MENUITEM" + editor\rootmt\id + " = WindowMenu( " + gt\name$ + " )"

		; do children
		CreateBlitzPlusMenus( editor\rootmt\childlist, stream )

		; show changes
		WriteLine stream, "UpdateWindowMenu " + editor\rootgt\name$
	EndIf

	; make loop
	If editor\exportloop
		CreateBlitzPlusLoop( stream )
	Else
		WriteLine stream, "While WaitEvent()<>$803"
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
Function CreateBlitzPlusMenus( ml.mlist, stream )

	; creates code for menu items in passed list
	; handles will look like MENUITEMitemname

	Local code$, label$

	; create menu items
	While ml <> Null

		mt.mtype = ml\mt

		label$ = "MENUITEM" + mt\id
		code$ = ""

		If mt\checkable = True Or mt\active = False
			code$ = "Global " + label$ + " = "
		ElseIf mt\childlist <> Null
			code$ = label$ + " = "
		EndIf

		code$ = code$ + "CreateMenu( " +Chr$(34)+ mt\label$

		If mt\shortcut$ <> ""
			Select mt\shortcutmod
				Case 0
					txt$ = Chr$(8) + "SHIFT+" + mt\shortcut$
				Case 1
					txt$ = Chr$(8) + "CTRL+" + mt\shortcut$
				Case 2
					txt$ = Chr$(8) + "ALT+" + mt\shortcut$
			End Select

			code$ = code$ + txt$
		EndIf

		code$ = code$ +Chr$(34) + "," + mt\id + "," + "MENUITEM" + mt\parent\id + " )"

		WriteLine stream, code$

		; do menu options

		If mt\active = False Then WriteLine stream, "DisableMenu " + label$
		If mt\checked = True Then WriteLine stream, "CheckMenu " + label$

		; do possible children
		If ml\mt\childlist <> Null Then CreateBlitzPlusMenus( ml\mt\childlist, stream )

		ml = ml\nxt

	Wend

End Function

Function CreateBlitzPlusCode( l.list, stream )

	; creates a line of code for each gadget in passed list

	Local style, indent$, layoutflags, custom, childlist

	If editor\indentedcode								; then set indent level and string
		editor\indentlevel = editor\indentlevel+1
		indent$ = String$( Chr$(9), editor\indentlevel )
	Else
		indent$ = ""
	EndIf

	;
	;walk through linked list
	While l <> Null

		gt.gtype = l\gt
		style = DetermineStyle( gt )

		; make children orient towards parent, not to GUIde editcanvas
		xpos = Int(gt\xpos - gt\parent\xpos)
		ypos = Int(gt\ypos - gt\parent\ypos)

		parent$ = gt\parent\name$
		If gt\parent\version = FOLDBOX1 Then parent$ = parent$ + "\opencanvas"

		; determine gadget type

		custom = False				; set if gadget is a custom gadget, so no layoutflags are set
		childlist = True			; set if gadget is not to be exported, and childlist neither

		Select gt\version
			Case SPLITTER
				; not supported in export yet
				custom = True
				childlist = False
			Case IMAGE
				code$ = indent$ + gt\name$+"=CreateImageBox(" + xpos +","+ ypos +","+ Chr$(34)+ StripPath$( gt\image$ ) + Chr$(34) + "," +parent+ ")"
				WriteLine stream, code$
			Case COLSELECTOR
				custom = True
				code$ = indent$ + gt\name$+".CSelector=CreateColorSelector(" + xpos +","+ ypos +","+ gt\width+ "," +gt\height+ "," + gt\r + "," + gt\g + "," + gt\b + "," + parent + ")"
				WriteLine stream, code$
				code$=indent$+ "	SetGadgetLayout "+gt\name$+"\canvas,"+gt\layoutleft+","+gt\layoutright+","+gt\layouttop+","+gt\layoutbottom
				WriteLine stream, code$
			Case FOLDBOX1
				custom = True
				code$ = indent$ + gt\name$+".foldbox=CreateFoldbox("+Chr$(34)+ gt\label$ +Chr$(34)+ "," +ypos+ ","+gt\height+","+gt\parent\name$+ ")"
				WriteLine stream, code$
			Case SPINNER
				custom = True
				code$ = indent$ + gt\name$+".spinner=CreateSpinner(" +xpos+ "," +ypos+ "," +gt\width+ "," +gt\height+ "," +parent$+ "," +gt\integer+ "," +gt\defaultstring$+ "," +gt\minval+ "," +gt\maxval+ "," +gt\stp+ "," +Chr$(34)+gt\prefix$+Chr$(34)+ "," +Chr$(34)+gt\suffix$+Chr$(34)+ ")"
				WriteLine stream, code$
				
				; layoutflags etc. code is different for this gadget, so do those settings here
				
				; set gadget layout flags
				If layoutflags = True
					code$=indent$+ "	SetGadgetLayout "+gt\name$+"\textgad,"+gt\layoutleft+","+gt\layoutright+","
					code$=code$+gt\layouttop+","+gt\layoutbottom
					WriteLine stream, code$
				EndIf

				; disable gadget if needed
				If gt\enable = False
					code$ = indent$+ "	DisableGadget "+gt\name$ + "\textgad"
					WriteLine stream, code$
					code$ = indent$+ "	DisableGadget "+gt\name$ + "\slider"
					WriteLine stream, code$
				EndIf

				; do not hide if parent is hidden as well
				If gt\hide = True And gt\parent\hide = False
					code$ = indent$+ "	HideGadget " + gt\name$+ "\textgad"
					WriteLine stream, code$
					code$ = indent$+ "	HideGadget " + gt\name$+ "\slider"
					WriteLine stream, code$
				EndIf
				
			Case GROUPBOX		; done
				code$ = indent$ + gt\name$+"=CreateGroup( "+Chr$(34)+ gt\label$ +Chr$(34)+ ","+xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$
				custom = True				; avoid blitzplus native gadget settings later on
			Case SEPLINE		; done
				code$ = indent$ + gt\name$+"=CreateLine( " + Chr$(34)+ gt\label$ +Chr$(34)+ ","+xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+","+ gt\direction +")"
				WriteLine stream, code$
				custom = True
			Case TREEVIEW		; done
				code$ = indent$ + gt\name$+"=CreateTreeView(" +xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$
			Case HTML			; done
				code$ = indent$ + gt\name$+ "=CreateHtmlView(" +xpos+","+ypos+","+ gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				If gt\defaulturl$ <> ""
					code$ = indent$+ "	HtmlViewGo " + gt\name$ +"," +Chr$(34)+ gt\defaulturl$ +Chr$(34)
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
				code$ = indent$+ "	SetGadgetText " +gt\name$+ "," +Chr$(34)+ gt\defaultstring$ +Chr$(34)
				WriteLine stream, code$

				; set backdrop color
				code$ = indent$+ "	SetTextAreaColor " + gt\name$ +","+ gt\r +","+ gt\g +","+ gt\b +",1"
				WriteLine stream, code$

				; set textcolor
				code$ = indent$+ "	SetTextAreaColor " + gt\name$ +","+ gt\textr +","+ gt\textg +","+ gt\textb +",0"
				WriteLine stream, code$

			Case BUTTON			; done
				code$ = indent$ + gt\name$+"=CreateButton("+Chr$(34)+gt\label$+Chr$(34)+","+xpos+","+ypos+"," + gt\width+","+gt\height+","+parent$+",1)"
				WriteLine stream, code$

			Case LABEL			; done

				; generate handle if needed
				If editor\labelhandles
					prefix$ = gt\name$ + "="
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
					code$ = indent$+ "	SetGadgetText " + gt\name$ +","+Chr$(34)+ gt\defaultstring +Chr$(34)
					WriteLine stream, code$
				EndIf

			Case CHECKBOX		; done
				code$ = indent$ + gt\name$+"=CreateButton("+Chr$(34)+gt\label$+Chr$(34)+","+xpos+","+ypos+","
				code$ = code$ + gt\width+","+gt\height+","+parent$+",2)"
				WriteLine stream, code$

				;turn on or off
				If gt\checked
					code$ = indent$+ "	SetButtonState " + gt\name$ +","+ gt\enable
					WriteLine stream, code$
				EndIf

			Case RADIOBUTTON	; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateButton("+Chr$(34)+gt\label$+Chr$(34)+","+xpos+","+ypos+"," + gt\width+","+gt\height+","+parent$+",3)"
				WriteLine stream, code$

				; turn on or off
				If gt\checked
					code$ = indent$+ "	SetButtonState " + gt\name$ +","+gt\enable
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

						code$ = indent$+ "	AddGadgetItem "+gt\name$+","+Chr$(34)+txt$+Chr$(34)
						WriteLine stream, code$
					EndIf
				Next

				; set default item
				code$ = indent$+ "	SelectGadgetItem " + gt\name$ +","+ gt\defaultitem
				WriteLine stream, code$

			Case TABBER			; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateTabber("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				; add items
				items$ = gt\items$
				laststart = 1
				itemcount = 0
				
				;
				;make sure there itemslist is delimited
				If Right$( items$, 1 ) <> ";" Then items$ = items$ + ";"

				For count = 1 To Len( items$ )
					If Mid$( items$, count, 1 ) = ";"
						lastsep = count
						txt$ = Mid$( items$, laststart, lastsep-laststart )
						laststart = count + 1

						; default item yes or no
						If itemcount = gt\defaultitem
							sel$ = ", True"
						Else
							sel$ = ", False"
						EndIf

						code$ = indent$+ "	AddGadgetItem "+gt\name$+","+Chr$(34)+txt$+Chr$(34)+ sel$
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
					code$ = indent$+ "	UpdateProgBar " + gt\name$ +","+ gt\progressval#
					WriteLine stream, code$
				EndIf

			Case CANVAS			; done
				; create gadget
				code$ = indent$ + gt\name$+"=CreateCanvas("+xpos+","+ypos+","+gt\width+","+gt\height+","+parent$+")"
				WriteLine stream, code$

				If gt\pointershow = False
					code$ = indent$+ "	HidePointer " + gt\name$
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
					code$ = indent$+ "	SetPanelColor "+gt\name$ +","+ gt\r +","+ gt\g +","+ gt\b
					WriteLine stream, code$
				EndIf

				; backdrop image?

				If gt\image$ <> ""
					code$ = indent$+ "	SetPanelImage " + gt\name$ +","+ Chr$(34)+ gt\image$ +Chr$(34)
					WriteLine stream, code$
				EndIf

				; is this panel oversized? if yes, create slider code alongside its parent

				CheckOversizedChild( gt, indent$, stream )

			Case TABPANEL
				;
				; if tabpanel is out of range(-1), then don't export it
				If gt\tabitem >= 0
					code$ = indent$ + gt\name$+"=CreatePanel(0,0,ClientWidth("+gt\parent\name$+"),ClientHeight("+gt\parent\name$+"),"+parent$+","+style+")"
					WriteLine stream, code$

					; hide if not on active tab item
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

		; export label if needed

		If gt\autolabel$ <> ""
			width = StringWidth( gt\autolabel$ )
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

			code$=indent$+ "	SetGadgetLayout "+gt\name$+","+gt\layoutleft+","+gt\layoutright+","
			code$=code$+gt\layouttop+","+gt\layoutbottom
			WriteLine stream, code$

			; disable gadget if needed
			If gt\enable = False
				code$ = indent$+ "	DisableGadget "+gt\name$
				WriteLine stream, code$
			EndIf

			; do not hide if parent is hidden as well
			If gt\hide = True And gt\parent\hide = False
				code$ = indent$+ "	HideGadget " + gt\name$
				WriteLine stream, code$
			EndIf

		EndIf

		; do possible children, unless childlist flag has been cleared (tabpanel)
		If l\gt\childlist <> Null And childlist = True Then CreateBlitzPlusCode( l\gt\childlist, stream )

		; go to next gadget in this childlist
		l = l\nxt
	Wend

	; go back one indention level

	editor\indentlevel = editor\indentlevel-1

End Function

Function CreateBlitzPlusLoop( stream )

	; creates code for a mainloop and event handling for the gadgets in this project

	; create main loop

	WriteLine stream, ""
	WriteLine stream, ";-mainloop--------------------------------------------------------------"
	WriteLine stream, ""

	WriteLine stream, "Repeat"
	WriteLine stream, "	id=WaitEvent()"
	WriteLine stream, "	Select id"

	If editor\foldboxcount <> 0 Or editor\colorcount <> o
		WriteLine stream, "		Case $201								; hit mouse button"

		If editor\foldboxcount
			WriteLine stream, "			CheckFoldBoxes( EventSource(), EventData() )"
		EndIf

		If editor\colorcount
			WriteLine stream, "			CheckColorSelectors( EventSource() )"
		EndIf

		WriteLine stream, "		Case $202								; released mouse button"
		WriteLine stream, "			GUIde\foldboxmove = False"
	EndIf

	If editor\splittercount
		WriteLine stream, "		Case $203									; move over canvas"
		WriteLine stream, "			Select EventSource()"

	; find splitters and call update function here

		For gt.gtype = Each gtype
			If gt\version = SPLITTER
				WriteLine stream, "				Case " + gt\name$ + "\canvas"
				WriteLine stream, "					UpdateSplitter(" + gt\name$ + ")"
			EndIf
		Next

		; close select statement

		WriteLine stream, "			End Select"
	EndIf

	WriteLine stream, "		Case $401									; interacted with gadget"
	WriteLine stream, "			DoGadgetAction( EventSource() )"
	WriteLine stream, "		Case $803									; close gadget"
	WriteLine stream, "			Exit"

	If editor\rootgt\menu = True
		WriteLine stream, "		Case $1001								; selected a menu"
		WriteLine stream, "		DoMenuAction( EventData() )"
	EndIf

	If editor\foldboxcount <> 0
		WriteLine stream, "		Case $4001								; timer tick"
		WriteLine stream, "			Select EventSource()"
		WriteLine stream, "				Case MouseTimer"
		WriteLine stream, "					If GUIde\foldboxmove = True Then ScrollFoldBoxes()"
		WriteLine stream, "			End Select"
	EndIf

	; hotkey event catchers here

	WriteLine stream, "	End Select"
	WriteLine stream, "Forever"
	WriteLine stream, ""

	; main loop done.
	; create clean up code after exiting loop

	; the deletion of splitters

	If editor\splittercount
		For gt.gtype = Each gtype
			If gt\version = SPLITTER Then WriteLine stream, "FreeSplitter( "+ gt\name$ + ")"
		Next
	EndIf

	WriteLine stream, ""

	; create DoGadgetAction() function

	WriteLine stream, ";-gadget actions--------------------------------------------------------"
	WriteLine stream, ""

	WriteLine stream, "Function DoGadgetAction( gadget )"
	WriteLine stream, "	Select gadget"

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
				WriteLine stream, "		Case " + gt\name$ + "	; user pressed button"
				WriteLine stream, ""

			Case CHECKBOX
				WriteLine stream, "		Case " + gt\name$ + "	; user changed checkbox"

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
					;
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
				WriteLine stream, "		Case " + gt\name$ + "	; user selected radiobox"
				;
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
				WriteLine stream, "			If EventData() = 13 Then	; user pressed return in textfield"
				WriteLine stream, "			EndIf"
				WriteLine stream, ""

			Case RADIOBUTTON
				WriteLine stream, "		Case " + gt\name$
				WriteLine stream, "			; insert your action for " + gt\name$ + " here"
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
					WriteLine stream, "			; insert your action for " + gt\name$ + " here"
				EndIf
				WriteLine stream, ""

			Case TREEVIEW
				WriteLine stream, "		Case " + gt\name$ + "	; user selected treeview node"
				WriteLine stream, ""

			Case TABBER
				If gt\itemcount > 1
					WriteLine stream, "		Case " + gt\name$ + "	; user changed tabber item"
					WriteLine stream, "			Select SelectedGadgetItem( " + gt\name$ + " )"
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
				WriteLine stream, "		Case " + gt\name$ + "	; user modified slider"
				WriteLine stream, ""
		End Select
	Next
	;
	; finish select statement
	WriteLine stream, "	End Select"
	;
	; add spinner control here if current project does contain one
	If editor\spinnercount > 0
		WriteLine stream, "	CheckSpinners( EventSource() )"
		WriteLine stream, ""
	EndIf

	WriteLine stream, "End Function"
	WriteLine stream, ""

	; create DoMenuAction() function here

	If editor\rootgt\menu = True

		WriteLine stream, ";-menu actions--------------------------------------------------------"
		WriteLine stream, ""

		WriteLine stream, "Function DoMenuAction( menuid )"
		WriteLine stream, "	Select menuid"

		; loop through menu items

		For mt.mtype = Each mtype

			; only export items at end of branch

			If mt\parent <> Null
				If mt\parent\version <> MENUROOT And mt\childlist = Null
					WriteLine stream, "		Case " + mt\id

					; check and uncheck code here

					If mt\checkable

						Local label$ = "MENUITEM" + mt\id

						WriteLine stream, "			If MenuChecked(" + label$ + ") = True"
						WriteLine stream, "				UnCheckMenu " + label$
						WriteLine stream, "			Else"
						WriteLine stream, "				CheckMenu " + label$
						WriteLine stream, "			EndIf"
						WriteLine stream, "			UpdateWindowMenu " + editor\rootGT\name$
					EndIf

					WriteLine stream, "			; insert action for menu item '" + mt\label$ + "' here."
					WriteLine stream, ""

				EndIf
			EndIf
		Next

		WriteLine stream, "	End Select"
		WriteLine stream, "End Function"
		WriteLine stream, ""

	EndIf

End Function

Function CheckOversizedChild( gt.gtype, indent$, stream )

	If gt\parent\largechildx

		; create horizontal slider

		slname$ = gt\parent\name$ + "SliderH"
		slx = gt\parent\xpos - gt\parent\parent\xpos
		sly = gt\parent\ypos - gt\parent\parent\ypos + gt\parent\height + 1;editor\grid\size

		slparent$ = gt\parent\parent\name$
		If gt\parent\parent\version = FOLDBOX1 Then slparent$ = slparent$ + "\opencanvas"

		code$ = indent$+editor\prefix$+slname$+"=CreateSlider("+slx+","+sly+","+gt\parent\width+","+16+","+slparent$+",1)"
		WriteLine stream, code$

		; set slider range

		code$ = indent$+ "	SetSliderRange " + slname$+",ClientWidth("+gt\parent\name$+"),"+gt\width
		WriteLine stream, code$

		; layout flags if needed.

		If editor\rootgt\resize
			code$=indent$+ "	SetGadgetLayout "+slname$+","+gt\parent\layoutleft+","+gt\parent\layoutright+"," + gt\parent\layouttop+","+gt\parent\layoutbottom
			WriteLine stream, code$
		EndIf
	EndIf

	If gt\parent\largechildy

		; create vertical slider

		slname$ = gt\parent\name$ + "SliderV"
		slx = gt\parent\xpos - gt\parent\parent\xpos + gt\parent\width + 1;editor\grid\size
		sly = gt\parent\ypos - gt\parent\parent\ypos; - gt\parent\ypos+gt\parent\height+editor\grid\size

		slparent$ = gt\parent\parent\name$
		If gt\parent\parent\version = FOLDBOX1 Then slparent$ = slparent$ + "\opencanvas"

		code$ = indent$+editor\prefix$+slname$+"=CreateSlider("+slx+","+sly+",16,"+gt\parent\height+","+slparent$+",2)"
		WriteLine stream, code$

		; set slider range

		code$ = indent$+ "	SetSliderRange " + slname$+",ClientHeight("+gt\parent\name$+"),"+gt\height
		WriteLine stream, code$

		; layout flags if needed

		If editor\rootgt\resize
			code$=indent$+ "	SetGadgetLayout "+slname$+","+gt\parent\layoutleft+","+gt\parent\layoutright+"," + gt\parent\layouttop+","+gt\parent\layoutbottom
			WriteLine stream, code$
		EndIf
	EndIf

End Function

Function CreateSplitterCode( stream )

	; creates the code for splitter export

	Local link%

	If editor\indentedcode								; then set indent level and string
		indent$ = String$( Chr$(9), 1 )
	EndIf

	; create splitter gadget creation code

	For gt.gtype = Each gtype

		If gt\version = SPLITTER

			; splitter found. create code for it

			; make gadget orient towards parent, not to editcanvas

			xpos = Int(gt\xpos - gt\parent\xpos)
			ypos = Int(gt\ypos - gt\parent\ypos)

			parent$ = gt\parent\name$
			If gt\parent\version = FOLDBOX1 Then parent$ = parent$ + "\opencanvas"

			; make code line

			code$ = indent$ + editor\prefix$ + gt\name$+".splitter=CreateSplitter(" + xpos +","+ ypos +","+ gt\width+ "," +gt\height+ "," + gt\direction + "," + parent + ")"
			WriteLine stream, code$

;			If editor\rootgt\resize
;				code$=indent$+ "	SetGadgetLayout "+gt\name$+"\canvas,"+gt\layoutleft+","+gt\layoutright+","
;				code$=code$+gt\layouttop+","+gt\layoutbottom
;				WriteLine stream, code$
;			EndIf

		EndIf
	Next

	; create linked gadgets code

	For gt.gtype = Each gtype
		If gt\version = SPLITTER

			; now, find gadgets belonging to splitters
			; look at position from the splitter and create the correct gadget int code
			; do not link gadget if it is NOT next to the splitter

			For g.gtype = Each gtype
				If g\group$ = gt\group$ And g <> gt

					; found a linked gadget

					link = True

					; see if it needs to be linked. a gadget has to be 'in line'
					; with the splitter.

					If gt\direction = 0
						If g\xpos + g\width < gt\xpos Then link = False
						If g\xpos > gt\xpos + gt\width Then link = False
					EndIf

					If link = True

						; get name

						name$ = g\name$

						; could be another splitter

						If g\version = SPLITTER Then name$ = name$ + "\canvas"

						code$ = indent$ + "	LinkToSplitter(" + name$ + "," + gt\name$

						If gt\direction = 0				; this is a horizontal splitter gadget
							If g\ypos < gt\ypos
								code$ = code$ + ",2)"
							Else
								code$ = code$ + ",3)"
							EndIf
						Else							; this is a vertical splitter gadget
							If g\xpos < gt\xpos
								code$ = code$ + ",0)"
							Else
								code$ = code$ + ",1)"
							EndIf
						EndIf

						WriteLine stream, code$

					EndIf

				EndIf
			Next

		EndIf

	Next

End Function

Function ExportGadgetDeclarationsPLUS(l.list, stream)
	;
	;exports gadget variables
	;calls again when gadget in list has a childlist
	
	While l <> Null
	
		temp$ = "Global " + l\gt\name$
		
		Select l\gt\version
			Case GROUPBOX
				temp$ = temp$ + ".groupbox"
			Case SPINNER
				temp$ = temp$ + ".Spinner"
			Case FOLDBOX1
				temp$ = temp$ + ".FoldBox"
			Case COLSELECTOR
				temp$ = temp$ + ".CSelector"
			Case SPLITTER
				temp$ = temp$ + ".Splitter"
		End Select
	
		WriteLine(stream, temp$)
		If l\gt\childlist <> Null Then ExportGadgetDeclarationsPLUS(l\gt\childlist, stream)
		;
		; go to next
		l = l\nxt	
	Wend
End Function

Function ExportMenuDeclarationsPLUS(l.mlist, stream)
	;
	;exports menu variables
	;calls again when menu in list has a childlist
	
	While l <> Null
	
		If l\mt\checkable = True Or l\mt\active = False
			WriteLine(stream, "Global " + l\mt\label$)		
;			code$ = "Global " + label$ + " = "
		EndIf

		If l\mt\childlist <> Null Then ExportMenuDeclarationsPLUS(l\mt\childlist, stream)
		;
		; go to next
		l = l\nxt	
	Wend
End Function
