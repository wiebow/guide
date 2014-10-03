; ==================================================================
; Project Title: GUIde
; Version: 1.4a
; Last change: 18 dec. 2005
; Author: wdw@home.nl
; ==================================================================

;#Region comments and logs
	
;log:
; + are additions, - are fixes or changes

; 1.4a
; + moved gadget variable name to top of exported code.
; + you can move a gadget which is partially outside its parent 'inside', even when 'constrain gadgets' is on.
; + added bmx to exported code extensions.
; + you can now delete tab panels! you can only delete tabpanels who have no children.
; - fixed colorselector bplus export
; - bplus custom gadgets are declared correctly now
; - bmax export now works correctly, even with superstrict
; - copy label does copy the label text.
; - tabbers start with one tab item now. it seems there are people out there who want to have one tab item. ok.
; - exportlanguage combobox selector now stays in place.
; - Updated decls file. There now is a Guide.decls in the res folder. Copy this to your blitzplus userlibs folder
;   You can remove the old decls file if you want to.

; 1.4
; + bliz max export added... some fine tuning required, but basics do work.
;   Just select bmax from the language combobox
; + text clipping on gadgets
; + added splitters! add a gadget to the same group as the splitter and it will move with it.
;	  Be cautious with the gadgetlayout parameters of the linked gadgets.
; - fixed some problems in the GadgetDelete function. More stable now
; - typing sizes in the edit window properties now resizes the edit window accordingly
; - gadgetcounters are reset properly now when creating or loading a project
; - the input box for a selected gadget was not hidden when you selected a menu item.
;   interacting with the surviving input box would cause a crash.

; 1.3
; + added icons to the gadget treeview. nice.
; + language selector combobox is added to the main window. the language combobox in the options window is
;	gone. More items will be added to the combobox once GUIde supports more languages.
; + expanded docs with new commands and features.
; + color selector and image gadgets added, export supported
; + height of textfield, combobox and spinner cannot be altered, and is set at 20.
; + realtime mouse pointer changes according to mode or selection.
; + Got rid of the toolwindow. Now there is a nice toolbar on the GUIde window.
; + added AppTitle support. Look for the property in the Edit Window.
; + added spinner functions to the export include:
;		SpinnerValue() : returns the value of the passed spinner.
;		EnableSpinner() : enables a spinner gadget.
;		DisableSpinner() : disables a spinner gadget.
; + CheckSpinners() now returns the handle of spinner interacted with.
; + added grouping for comboboxes and panels. Select a combobox item and the corresponding panel is shown,
;	  so you can show groups of gadgets when a certain option is selected. Panel groupnames should
;	  be the same as the combobox groupname, but with a number suffix corresponding to the combobox item.
; + you can now select a hidden gadget from the gadget tree and unhide it with CTRL \
;     If you have no selection, then ALL hidden gadgets will be unhidden.
; - you can only select tabbers, groupboxes and foldboxes at their titlebar now.
; - foldboxes move with parent panel
; - deleting a gadget would also disable the menu on/off check if the menu has no items. fixed.
; - radiobutton now centers on height, just like checkboxes do.
; - resizing GUIde window no longer updates editwindow.
; - modified display of tabbers, panels and groupboxes in GUIde. Grid now shows through.
; - modified exported spinner layout a bit. It aligns better to other gadgets now.
; - buttons don't stretch anymore when main window is resized.
; - fixed spinner int and float display.
; - changed yoffset of quick labels. they now align to the text in the gadget.
; - tabpanels now get exported with clientwidth and clientheight dimensions.
;
; 1.2
; + auto width adjustment of label, radiobutton and checkbox gadgets.
; + quick labels for combobox, textfield, progressbar and spinner gadgets.
;		These gadgets contain an extra 'label' property. The labels are exported as extra label gadgets.
; + spinners! including prefix and suffix strings. export includes all control code.
; + status bar is drawn when enabled.
; + oversized canvasses or panels on panels:
;		Sliders will appear next to the panel, and these will be exported and handled correctly.
; + text is now automatically selected in textboxes. Tell me if this should be an option yes or no.
; + 'reset' button for panels and textareas colors.
; + modify menu order. Select the menu from the gadget tree and press CTRL left or right.
; + foldable groupboxes! Can only be added to panels.
;		You can add as many foldboxes to the panel as you want.
;   	Select and press CTRL up or down to change foldbox gadget order.
;		For now, you cannot scroll the boxes. So create your boxes, and downsize your main window when done.
; - panels don't export backdrop color if it is the same as the system backdrop color.
; - modified exit message.
; - modified keystroke event in export loop a bit.
; - GUIde window can be minimised. You can now also resize the window.
; - fixed select bug: all gadgets are now de-selected when loading a new project
; - fixed move bug when child gadget is same size of parent, and a new position value is ENTERED.
; - fixed slider 'groupname' property.
; - fixed gadget move bug when you moved several gadgets at the same time. If one of the gadgets was
;		restrained it stopped (as it should), but the rest of the selection kept moving. This could
;		seriously ruin your layout. Now, ALL gadget movement is checked before the move is made.
; - fixed some small annoying bugs
;
; 1.1
; + proper docs...
; + rmb+drag now starts selection box. this makes it possible to multiple select gadgets on tabbers, etc
; + tabbers are interactive now!
; + tabber panels management is no longer optional. it's the only proper way to work with tabbers in b+ anyway
;   !!tabpanels with the item count -1 will not be exported. they are kept in the project though.
; + option screen now using the custom groupbox gadgets.. nice
; + include file for custom gadgets drawing and behaviour
; + custom gadgets: groupboxes and divider lines
; + groups. you can group radiobuttons and checkboxes with other gadgets
; + menus! no shortcut keys yet though.
; + export of gadget and menu actions. needs to be expanded as development goes on
; + export of main loop
; + resizing slider when orientation of gadget is changed
; + versioning in project saves. no more broken projects after updates!
; + movings side of gadgets snaps them to grid too
; - added check for negative grid size
; - quick saving stores filename, so next quicksave will actually work
; - numeric moving of gadgets moves children too
; - fixed double movement of child gadgets when parent and child are selected
; - cloning not increasing gadgetname number
; - quicksave asks for filename if current project has not been saved before
; - minor export issues
;
; 1.0
; - editwindow renames properly after starting a new project
; - fixed snaptogrid. no more moving inbetween grid positions
; - fixed saving of tabberpanel settings
; - fixed quicksave via menu item

;------------------------------------------------------------------

; known bugs:
; gadgets on tabbers cannot be hidden... logisch, want een tab selectie un-hide alle gadgets op de tabpanel
; entering a window size doesnt resize the edit window
; in export: checkbox in zelfde groep zetten elkaar aan en uit

; to do:
;	htmlview flags
; 	change toggable option when clicking on them in the tree?? or image requestor
;	menu shortcut texts
;	minimum window size
;	enable groupbox tick next to groupbox label ?
;	non-move mode

;	gadgets: splitters, image buttons
;	foldbox: scroll in guide
;	export to clipboard. need to change export to one large string to send to stream or clipboard
;	menu shortcuts in export
;	EXPORT SELECTED
;	drag gadgets off parent

;#End Region

Include "includes\grib.bb"					; grid functions
Include "includes\math2d.bb"				; handy math

Include "guideexportinclude.bb"		; export include.... hier moet ik vanaf. wat gebruik ik in main editor?? groupbox

Include "GUIDEconstants.bb"					; all constants
Include "UserInterface.bb"					; builds the GUIDE interface
Include "GadgetsMain.bb"					; form gadget creation and handling
Include "GadgetsDraw.bb"					; form gadgets draw functions
Include "MenusMain.bb"						; form menus creation and handling
Include "LinkedLists.bb"					; linked lists creation and handling

Include "BlitzPlusExport.bb"				; export form to bplus code
Include "MaxExport.bb"							; export form to bmax code
;include "WinBlitz3DExport.bb"				

SetUpEditor()
MainLoop()
End

Function MainLoop()
	Repeat
		id = WaitEvent()
		Select id
			Case $101, $102 ;EVENT_KEYUP, EVENT_KEYDOWN
				DetermineMode()
			Case $201; EVENT_MOUSEDOWN
				MousePress(EventData())
			Case $202;EVENT_MOUSEUP
				MouseRelease(EventData())
			Case $203;EVENT_MOUSEMOVE
				DoMouseAction(EventSource())
			Case $205;EVENT_MOUSEENTER
				editor\activecanvas = EventSource()
			Case $206;EVENT_MOUSELEAVE
				Select editor\activecanvas
					Case PLUSCANVAS
						editor\activecanvas = 0
						UpdateCanvas(PLUSCANVAS)
					Case MAXCANVAS
						editor\activecanvas = 0
						UpdateCanvas(MAXCANVAS)
				End Select					
			Case $401;EVENT_GADGETACTION
				DoGadgetAction(EventSource())
				UpdateCanvas(DISPLAYCANVAS)
			Case $802;EVENT_WINDOWSIZE
				If EventSource() = EDITWINDOW Then
					ResizeWindow()
					; pass new size to edit windows gadget type
					editor\rootGT\width = GadgetWidth( DISPLAYCANVAS )
					editor\rootGT\height = GadgetHeight( DISPLAYCANVAS )
				EndIf

			Case $803;EVENT_WINDOWCLOSE
				CloseWindow(EventSource())
			Case $1001;EVENT_MENUEEVENT
				DoMenuAction(EventData())
		End Select
	Forever
End Function

Function SetUpEditor()
	; creates editor type, grid, and editwindow gtype

	editor.editor = New editor
	editor\font = LoadFont("Tahoma",13 )
	editor\mode = MODE_NORMAL
	editor\selection = Null

	; create grid
	editor\grid = GR_CreateGrid( "", True, 1 )
	editor\grid\size = 8
	editor\grid\zoom = 1

	; do default settings
	editor\changed = False
	editor\constraint = True
	editor\drawgrid = True
	editor\snaptogrid = True
	editor\labelhandles	= True
	editor\indentedcode	= True
	editor\showlabels = False
	editor\prefix$="Global "
	editor\exportloop = True
	editor\menuidcount = 1

	; try to load user settings
	SettingsLoad()

	; set up hotkeys
	HotKeyEvent 1,0,$1001, 99				; ESC
	HotKeyEvent 211,2, $1001,98				; ctrl DEL
	HotKeyEvent 12, 2, $1001,97				; ctrl -
	HotKeyEvent 43, 2, $1001,96				; ctrl \
	HotKeyEvent 59,0,$1001,95				; F1
	HotKeyEvent 24,2,$1001,94				; ctrl-o
	HotKeyEvent 30,2,$1001,93				; CTRL A
	HotKeyEvent 57,2,$1001,92				; CTRL-SPACE
	HotKeyEvent 200,2,$1001, 91				; up
	HotKeyEvent 208,2,$1001, 90				; down
	HotKeyEvent 203,2,$1001, 89				; left
	HotKeyEvent 205,2,$1001, 88				; right
	HotKeyEvent 31,2,$1001,87				; CTRL-S
	HotKeyEvent 15,2,$1001,86				; CTRL-TAB
	HotKeyEvent 46,2,$1001,85				; CTRL-c
	HotKeyEvent 47,2,$1001,84				; CTRL-v
	
	;
	;

	; get system colors

	col=api_GetSysColor(COLOR_3DFACE)
	editor\gadr=col And $ff
	editor\gadg=col Shr 8 And $ff
	editor\gadb=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_BTNTEXT)
	editor\gad_text_r=col And $ff
	editor\gad_text_g=col Shr 8 And $ff
	editor\gad_text_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_3DHILIGHT)
	editor\gad_hilite_r=col And $ff
	editor\gad_hilite_g=col Shr 8 And $ff
	editor\gad_hilite_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_3DSHADOW)
	editor\gad_shade_r=col And $ff
	editor\gad_shade_g=col Shr 8 And $ff
	editor\gad_shade_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_3DDKSHADOW)
	editor\gad_dshade_r=col And $ff
	editor\gad_dshade_g=col Shr 8 And $ff
	editor\gad_dshade_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_WINDOW)
	editor\winr=col And $ff
	editor\wing=col Shr 8 And $ff
	editor\winb=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_WINDOWTEXT)
	editor\win_text_r=col And $ff
	editor\win_text_g=col Shr 8 And $ff
	editor\win_text_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_HIGHLIGHT)
	editor\sel_text_r=col And $ff
	editor\sel_text_g=col Shr 8 And $ff
	editor\sel_text_b=col Shr 16 And $ff

	; force grid backdrop to same color as windows backdrop color
	editor\grid\br = editor\gadr
	editor\grid\bg = editor\gadg
	editor\grid\bb = editor\gadb

	; create edit window
	editor\RootGt = CreateGadget( WINDOW )
	editor\rootGT\xpos = 0
	editor\rootGT\ypos = 0

	; select edit window
	LL_AddItem( editor\rootGT, editor\selection )
	ShowGadgetDetails( editor\rootGT )
	SelectTreeViewNode editor\rootGT\node

	UpdateCanvas(DISPLAYCANVAS)
	UpdateCanvas(PLUSCANVAS)
	UpdateCanvas(MAXCANVAS)
	ActivateWindow MAINWINDOW
End Function

Function SettingsLoad()

	; try to load guide.ini

	stream = ReadFile( "guide.ini" )
	If stream
		version$ = ReadString( stream )
		editor\grid\size = ReadByte( stream )
		editor\constraint = ReadByte( stream )
		editor\drawgrid = ReadByte( stream )
		editor\snaptogrid = ReadByte( stream )
		editor\fileextension = ReadByte( stream )
		editor\labelhandles	= ReadByte( stream )
		editor\indentedcode	= ReadByte( stream )
		editor\showlabels = ReadByte( stream )
		editor\exportloop = ReadByte( stream )

		; settings additions per version here
		Select version$
			Case "1.4"
				editor\exporttype$ = ReadByte(stream)
		End Select
		CloseFile stream
	EndIf

	; set gadgets in options window to settings in editor
	
	SetButtonState chbCONSTRAIN, editor\constraint
	SetButtonState chbINDENT, editor\indentedcode
	SetButtonState chbHANDLES, editor\labelhandles
	SetButtonState chbLABELS, editor\showlabels
	SelectGadgetItem cobEXTENSION, editor\fileextension
	SetButtonState chbGRID, editor\drawgrid
	SetButtonState chbSNAP, editor\snaptogrid
	SetGadgetText tfdGRIDSIZE, editor\grid\size
	;SetGadgetText tfdPREFIX, editor\prefix$
	SetButtonState chbMAINLOOP, editor\exportloop
	SelectGadgetItem EXPORT_TYPE, editor\exporttype

	Select editor\exporttype
		Case 0
			ShowGadget PLUSCANVAS
			HideGadget MAXCANVAS
		Case 1
			ShowGadget MAXCANVAS
			HideGadget PLUSCANVAS
	End Select				

	
End Function

Function SettingsSave()

	; try to save settings
	stream = WriteFile( "guide.ini" )
	If stream
		WriteString stream, APPVERSION$
		WriteByte stream, editor\grid\size
		WriteByte stream, editor\constraint
		WriteByte stream, editor\drawgrid
		WriteByte stream, editor\snaptogrid
		WriteByte stream, editor\fileextension
		WriteByte stream, editor\labelhandles
		WriteByte stream, editor\indentedcode
		WriteByte stream, editor\showlabels
;		WriteString stream, editor\prefix$
		WriteByte stream, editor\exportloop
		WriteByte stream, SelectedGadgetItem(EXPORT_TYPE)
		CloseFile stream
	Else
		Notify "Unable to save settings!"
	EndIf
End Function

Function DetermineMode()

	; changes editor mode based on keys held ( or not )
	If KeyDown( 29 ) Or KeyDown( 157 )		; ctrl
		editor\mode = MODE_MULTISELECT
		Return
	EndIf

	; no keys pressed. set normal mode
	editor\mode = MODE_NORMAL
End Function

Function CloseWindow( source )

	; closes windows or editor
	Select source
		Case MAINWINDOW
			Local ok = True

			If editor\changed = True
				ok = Proceed( "Exit GUIde..."+Chr$(10)+Chr$(13)+"Do you want to save changes first?", 1 )
				If ok = -1 Then Return					; return to guide if user pressed Cancel
				If ok = 1 Then ProjectSave()			; save first if user pressed Yes
			EndIf

			; quit
			SettingsSave()
			FreeTreeViewNode editor\rootgt\node
			Delete Each gtype
			Delete Each list
			Delete editor
			FreeGadget mainwindow
			End

		Case OPTIONWIN
;			editor\prefix$ = TextFieldText( tfdPREFIX )

			; check for illegal grid size
			val = Int( TextFieldText( tfdGRIDSIZE ))
			If val < 1 Then val = 4
			editor\grid\size = val
			SetGadgetText tfdGRIDSIZE, val
			HideGadget OPTIONWIN
	End Select
End Function

Function UpdateCanvas( canvas )

	; updates the active canvas

	If canvas = 0 Then Return

	SetBuffer CanvasBuffer( canvas )
	ClsColor editor\gadr, editor\gadg, editor\gadb
	Cls

	Select canvas
		Case DISPLAYCANVAS

		Local ml.mlist, l.list

		; draw grid if needed

		If editor\drawgrid; And editor\grid\size > 1
			LockBuffer CanvasBuffer( canvas )
			y = 0
			While y < GadgetHeight( canvas )
				x = 0
				While x < GadgetWidth( canvas )
					WritePixelFast x,y, 0
					Plot x,y
					x = x + editor\grid\size
				Wend
				y = y + editor\grid\size
			Wend
			UnlockBuffer CanvasBuffer( canvas )
		EndIf

		; reset gadget draw flag

		For gt.gtype = Each gtype
			gt\drawn= False
		Next

		; draw gadgets

		SetFont editor\font
		DrawGUIgadget( editor\rootGT )

		; draw statusbar

		If editor\rootgt\statusbar = True

			width = ClientWidth( canvas )
			ypos = ClientHeight( canvas ) - 18

			Color editor\gadr, editor\gadg, editor\gadb
			Rect 0, ypos-2, width, 20,1

			Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
			Line 0, ypos, width, ypos
			Line 0, ypos, 0, ypos + 18

			Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
			Line 0,ypos+17, width, ypos+17
			Line width,ypos+1, width, ypos+17

		EndIf

		; draw gadget handles. walk through selection list
		; do not draw main window and tabberpanel selection boxes!

		l = editor\selection

		While l <> Null

			If l\gt <> editor\rootgt And l\gt\version <> TABPANEL

				xpos = l\gt\xpos
				ypos = l\gt\ypos
				width = l\gt\width
				height = l\gt\height

				; select the color of the boxes according to gadget type

				Color 0,0,0

				If l\gt\version = IMAGE Or l\gt\version = FOLDBOX1
					Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
				EndIf

				Rect xpos-6, ypos+height/2-3, 6,6,1
				Rect xpos+width, ypos+height/2-3, 6,6,1

				If l\gt\version = TEXTFIELD Or l\gt\version = COMBOBOX Or l\gt\version = SPINNER Or l\gt\version = FOLDBOX1
					Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
				EndIf

				Rect xpos-6, ypos-6, 6,6,1
				Rect xpos-6, ypos+height, 6,6,1
				Rect xpos+width, ypos-6, 6,6,1
				Rect xpos+width, ypos+height, 6,6,1
				Rect xpos+width/2-3, ypos-6, 6,6,1

				If l\gt\version = FOLDBOX1 Then Color 0,0,0

				Rect xpos+width/2-3, ypos+height, 6,6,1
			EndIf

			l = l\nxt
		Wend

		; draw selection box

		If editor\mode = MODE_BOX
			Color editor\gad_shade_r,editor\gad_shade_g,editor\gad_shade_b

			xpos = editor\bx
			ypos = editor\by
			width = editor\grid\cx - editor\bx
			height = editor\grid\cy - editor\by

			If width < 0
				xpos = editor\bx + width
				width = Abs(width)
			EndIf

			If height < 0
				ypos = editor\by + height
				height = Abs(height)
			EndIf

			Rect xpos,ypos, width, height, 0
		EndIf

		FlipCanvas canvas

	Case MENUCANVAS

		; update menu canvas

		SetFont editor\font
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b

		ml = editor\rootmt\childlist

		xpos = 6

		While ml <> Null
			Text xpos,0, ml\mt\label$
			xpos = xpos + StringWidth( ml\mt\label$ ) + 12
			ml = ml\nxt
		Wend

		FlipCanvas canvas

	Case PLUSCANVAS, MAXCANVAS
		SetFont editor\font
		Color 0,0,0

		If CANVAS = PLUSCANVAS
			DrawImage PLUSTOOLIMG, 0,0
		Else
			DrawImage MAXTOOLIMG, 0,0
		EndIf

		Text 22, 2,    "Button"
		Text 22, 16+2, "Label"
		Text 22, 32+2, "TextField"
		Text 22, 48+2, "TextArea"
		Text 22, 64+2, "CheckBox"
		Text 22, 80+2, "RadioButton"
		Text 22, 96+2, "ListBox"
		Text 22,112+2, "ComboBox"
		Text 22,128+2, "TreeView"
		Text 22,144+2, "Tabber"
		Text 22,160+2, "ProgressBar"
		Text 22,176+2, "Canvas"
		Text 22,192+2, "Panel"
		Text 22,208+2, "Slider"
		Text 22,224+2, "HTMLView"
		Text 22,240+2, "GroupBox"
		
		If CANVAS = PLUSCANVAS
			Text 22,256+2, "Spinner"
			Text 22,272+2, "Line"
			Text 22,288+2, "FoldBox"
			Text 22,304+2, "Image"
			Text 22,320+2, "ColorSelector"
			Text 22,336+2, "Splitter"
		EndIf

		; determine place of pointer on canvas, draw mouseover rect

		If editor\activecanvas = PLUSCANVAS Or editor\activecanvas = MAXCANVAS

			xpos = 0
			ypos = Int(MouseY(CANVAS) / 16 ) * 16

			; draw sunken box if mouse button is pressed

			If editor\lmb = True
				Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
				Line xpos,ypos, 95,ypos
				Line xpos, ypos, xpos, ypos + 16

				Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
				Line xpos, ypos + 16, xpos + 95, ypos + 16
				Line xpos + 95, ypos, xpos + 95, ypos + 16
			Else
				Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
				Line xpos,ypos, 95,ypos
				Line xpos, ypos, xpos, ypos + 16

				Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
				Line xpos, ypos + 16, xpos + 95, ypos + 16
				Line xpos + 95, ypos, xpos + 95, ypos + 16
			EndIf
		EndIf

		FlipCanvas canvas

	End Select

End Function

Function MousePress( button )

	; sees which button was pressed and where, take appropriate action

	Local l.list, gt.gtype, child.gtype

	If button = 1

		editor\lmb = True

		Select editor\activecanvas
			Case DISPLAYCANVAS

				; see if we hit the corner handle of a selected gadget
				; if yes, go to move mode

				l = editor\selection

				While l <> Null
					If CornerHit( l\gt )
						editor\mode = editor\sidehandle
						Return
					EndIf

					l = l\nxt
				Wend

				; no corners hit. see if we hit a gadget in the childlist of the edit window

				; see if we hit a gadget
				; start at the back of the list

				gt = Last gtype

				While gt <> Null

					If GadgetHit( MouseX( DISPLAYCANVAS ), MouseY( DISPLAYCANVAS ), gt )

						; hit

						; check childlist of hit gadget for more hits

						gt2.gtype = GadgetChildHit( MouseX( DISPLAYCANVAS ), MouseY( DISPLAYCANVAS ), gt\childlist )
						If gt2 <> Null Then gt = gt2

						; get rid of old selections if ctrl key not held

						If editor\mode <> MODE_MULTISELECT Then LL_ClearList( editor\selection )

						; add to selection list
						LL_AddItem( gt, editor\selection )

						; if we have selected a tabber, see if we clicked on a tab and change it.
						If gt\version = TABBER Then SelectTabberItem( gt )

						; show results

						ShowGadgetDetails( gt )
						SelectTreeViewNode gt\node
						editor\mode = MODE_MOVE
						UpdateCanvas(DISPLAYCANVAS)

						Return
					EndIf

					gt = Before gt
				Wend

				; we've hit nothing at all
				; clear properties and gadgets

				HideInputGadgets()
				SetGadgetText VERSIONLABEL, "Type:"
				ClearGadgetItems GADGETPROPBOX
				SetStatusText MAINWINDOW, ""

				If editor\mode <> MODE_MULTISELECT Then LL_ClearList( editor\selection )
				LL_ClearMenuList( editor\mselection )

			Case PLUSCANVAS
				; create the gadget we clicked on
				CreateGadget( Int(MouseY(PLUSCANVAS) / 16 ) )
				UpdateCanvas(PLUSCANVAS)
				UpdateCanvas(DISPLAYCANVAS)

			Case MAXCANVAS
				CreateGadget( Int(MouseY(MAXCANVAS) / 16 ) )
				UpdateCanvas(MAXCANVAS)
				UpdateCanvas(DISPLAYCANVAS)

		End Select
	Else
		editor\rmb = True

		; get rid of old selection if ctrl key not held
		If editor\mode <> MODE_MULTISELECT Then LL_ClearList( editor\selection )

		editor\mode = MODE_BOX
		editor\bx = MouseX( DISPLAYCANVAS )
		editor\by = MouseY( DISPLAYCANVAS )
		UpdateCanvas(DISPLAYCANVAS)
	EndIf

End Function

Function MouseRelease( button )

	; re-sets editor mode when mouse button gets released.
	; it also does some actions according to mode

	Local l.list
	Local xmoved, ymoved
	If button = 1
		editor\lmb = False
		Select editor\activecanvas
			Case DISPLAYCANVAS
				Select editor\mode
					Case MODE_BOX, MODE_NORMAL		; no action here

					Case MODE_MOVE
						;
						; only snap origin of 1st gadget, as width and height already should be snapped
						l = editor\selection
						If editor\snaptogrid
							; snap gadget origin to grid
							SnapPosToGrid( l\gt\xpos, l\gt\ypos )

							; new position is located in globals.
							; determine how much gadget moved
							xmoved = GR_ResultX - l\gt\xpos
							ymoved = GR_ResultY - l\gt\ypos
						EndIf

						; move rest of selection list
						MoveGadgets( xmoved, ymoved, l )
						ShowGadgetDetails( editor\selection\gt )

					Default		; moving a side of the gadget
						l = editor\selection
						If editor\snaptogrid

							; snap gadget origin to grid
							SnapPosToGrid( l\gt\xpos, l\gt\ypos )

							; new position is located in globals.
							; determine how much gadget moved
							xmoved = GR_ResultX - l\gt\xpos
							ymoved = GR_ResultY - l\gt\ypos
							SnapGadgetToGrid( l\gt )

							; move possible children
							MoveGadgets( xmoved, ymoved, l\gt\childlist )
						EndIf

						ShowGadgetDetails( editor\selection\gt )
				End Select
		End Select
	Else
		editor\rmb = False
	EndIf

	DetermineMode()
	UpdateCanvas( editor\activecanvas )
End Function

Function DoMenuAction( id )

	; checks menu events and hotkey events

	Select id

		; menus

		Case FILENEW
			ProjectNew()

			SetGadgetShape EDITWINDOW, GadgetX(MAINWINDOW) + GadgetWidth(MAINWINDOW), GadgetY(MAINWINDOW), 400,400
			ResizeWindow()

		Case FILEOPEN
			ProjectLoad()

		Case FILESAVEAS
			ProjectSave()

		Case FILEEXPORT
			If DEMO = False Then ExportCode()

		Case FILEEXIT
			CloseWindow( MAINWINDOW )

		Case HELPABOUT
			Notify "GUIde, a Form Editor" +Chr$(13)+Chr$(10)+ "http://www.playerfactory.co.uk"

		Case HELPKEYS
			ShowGadget HELPWINDOW

		Case VIEWOPTIONS
			ShowGadget OPTIONWIN

		; hotkeys or menus

		Case 99											; ESC		de-select all
			LL_ClearList( editor\selection )
			ClearGadgetItems GADGETPROPBOX
			UpdateCanvas( DISPLAYCANVAS )

		Case 98, EDITDELETE								; DEL		delete selection
			DeleteList(editor\selection)
			DeleteMenuItems()
			UpdateCanvas( DISPLAYCANVAS )
			UpdateCanvas( MENUCANVAS )

		Case 97											; -			hide selection
			HideSelection( editor\selection )
			UpdateCanvas( DISPLAYCANVAS )

		Case 96											; \			unhide gadgets
			If editor\selection <> Null
				UnhideSelection( editor\selection )
			Else
				UnhideGadgets()
			EndIf

			UpdateCanvas( DISPLAYCANVAS )

		Case 95											; F1
			ShowGadget HELPWINDOW

		Case 94
			ShowGadget OPTIONWIN

		Case 93, EDITSELECTALL							; CTRL A or selectall menu

			; delete current selection

			LL_ClearList( editor\selection )
			SetStatusText MAINWINDOW, ""

			; add all gadgets to selection

			For gt.gtype = Each gtype
				If gt <> editor\rootgt Then LL_AddItem( gt, editor\selection )
			Next

			UpdateCanvas( DISPLAYCANVAS )

		Case 92, EDITCLONE
			CloneGadget()												;	CTRL-SPACE or clone menu
			UpdateCanvas( DISPLAYCANVAS )
		Case 91															; CTRL-UP

			If editor\selection <> Null
				If editor\selection\gt\version = FOLDBOX1
					MoveFoldBoxGadget( editor\selection\gt, 0 )
				Else
					MoveGadgets( 0, -editor\grid\size, editor\selection, False, True )
				EndIf
				UpdateCanvas(DISPLAYCANVAS)
			EndIf

		Case 90															; CTRL-DOWN
			If editor\selection <> Null
				If editor\selection\gt\version = FOLDBOX1
					MoveFoldBoxGadget( editor\selection\gt, 1 )
				Else
					MoveGadgets( 0, editor\grid\size, editor\selection, False, True )
				EndIf
				UpdateCanvas(DISPLAYCANVAS)
			EndIf

		Case 89															; CTRL-LEFT
			If editor\mselection <> Null
				MoveMenu( -1 )
			Else
				MoveGadgets( -editor\grid\size, 0, editor\selection )
			EndIf
			UpdateCanvas(DISPLAYCANVAS)

		Case 88															; CTRL-RIGHT
			If editor\mselection <> Null
				MoveMenu( 1 )
			Else
				MoveGadgets( editor\grid\size, 0, editor\selection )
			EndIf
			UpdateCanvas(DISPLAYCANVAS)

	 	Case 87, FILESAVE												; CTRL-S
	 		ProjectSave( True )

	 	Case 86															; CTRL-INS
	 		CreateMenuItem()
			UpdateCanvas( MENUCANVAS )
			
		Case 85									; CTRL-v
		;	Stop
			
	End Select

End Function

Function DoGadgetAction( gadget )

	; takes action according to gadget pressed

	Select gadget
		Case EXPORT_TYPE						; selected another tool bar
			Select SelectedGadgetItem(EXPORT_TYPE)
				Case 0
					ShowGadget PLUSCANVAS
					HideGadget MAXCANVAS
				Case 1
					ShowGadget MAXCANVAS
					HideGadget PLUSCANVAS
			End Select				

		Case PROPTEXT						; pressed enter in text property gadget
			If EventData() = 13
				StorePropInput()
				editor\mode = MODE_NORMAL
			EndIf

		Case PROPCOMBO, PROPSELECT			; changed the value of a property button
			StorePropInput()
			editor\mode = MODE_NORMAL

		Case GADGETTREE

			; clicked on the gadgettree gadget....
			; get rid of old selections
			LL_ClearList(editor\selection)
			LL_ClearMenuList(editor\mselection)

			Local node = SelectedTreeViewNode( GADGETTREE )

			; find the gadget containing this node
			For gt.gtype = Each gtype
				If gt\node = node
					LL_AddItem(gt, editor\selection, LL_SELECTION)
					ShowGadgetDetails(gt, True)
					Return
				EndIf
			Next

			; still here? we must have selected a menu. find it

			For mt.mtype = Each mtype
				If mt\node = node
					LL_ClearMenuList(editor\mselection)
					LL_AddItemMenu(mt, editor\mselection, LL_SELECTION)
					HideInputGadgets()
					ShowMenuDetails(mt)
				EndIf
			Next

		Case GADGETPROPBOX
			If editor\selection <> Null
				CreatePropInput( SelectedGadgetItem( GADGETPROPBOX ) )	; create the correct input gadget for this gadget property
			Else
				CreateMenuPropInput( SelectedGadgetItem( GADGETPROPBOX ) )
			EndIf

		Case PANELCOL

			; select button pressed. can be used for panelcolor and imagebox

			Select editor\selection\gt\version
				Case COLSELECTOR, PANEL
					RequestColor( editor\selection\gt\r,editor\selection\gt\g, editor\selection\gt\b )
					editor\selection\gt\r = RequestedRed()
					editor\selection\gt\g = RequestedGreen()
					editor\selection\gt\b = RequestedBlue()
					ShowGadgetDetails( editor\selection\gt )

				Case IMAGE

					gt.gtype = editor\selection\gt
					gt\image$ = RequestFile( "Select an image...","bmp,png,jpg,pcx" )

					If gt\image$ <> ""
						gt\imagehandle = LoadImage( gt\image$ )
						gt\width = ImageWidth( gt\imagehandle)
						gt\height = ImageHeight( gt\imagehandle)
						ShowGadgetDetails( gt )
					EndIf

			End Select

		Case DEF

			; check which gadget color we are restoring to default and get the correct color

			Select editor\selection\gt\version
				Case PANEL
					r = editor\gadr
					g = editor\gadg
					b = editor\gadb
				Case TEXTAREA
					r = editor\winr				; backdrop col
					g = editor\wing
					b = editor\winb
			End Select

			editor\selection\gt\r = r
			editor\selection\gt\g = g
			editor\selection\gt\b = b
			ShowGadgetDetails( editor\selection\gt )

		Case PANELIMG
			editor\selection\gt\image$ = RequestFile( "Select backdrop image", "" )

		Case AREACOL
			RequestColor( editor\selection\gt\r,editor\selection\gt\g, editor\selection\gt\b )
			editor\selection\gt\r = RequestedRed()
			editor\selection\gt\g = RequestedGreen()
			editor\selection\gt\b = RequestedBlue()
			ShowGadgetDetails( editor\selection\gt )

		Case AREATXT
			RequestColor( editor\selection\gt\r,editor\selection\gt\g, editor\selection\gt\b )
			editor\selection\gt\textr = RequestedRed()
			editor\selection\gt\textg = RequestedGreen()
			editor\selection\gt\textb = RequestedBlue()
			ShowGadgetDetails( editor\selection\gt )

		Case HELPOK
			HideGadget HELPWINDOW

		; menu item buttons here

		Case MENUTXT
			If EventData() = 13 Then StoreMenuInput()

		Case MENUCHECK, MENUCOMBO
			StoreMenuInput()

		; option window gadgets here. change settings in editor type

		Case btnDONE
			CloseWindow( OPTIONWIN )

		Case chbCONSTRAIN
			editor\constraint = ButtonState( chbCONSTRAIN )

		Case tfdPREFIX
			If EventData() = 13 Then editor\prefix$ = TextFieldText( tfdPREFIX )

		Case chbINDENT
			editor\indentedcode = ButtonState( chbINDENT )

		Case chbHANDLES
			editor\labelhandles = ButtonState( chbHANDLES )

		Case chbSNAP
			editor\snaptogrid = ButtonState( chbSNAP )

		Case chbGRID
			editor\drawgrid = ButtonState( chbGRID )

		Case tfdGRIDSIZE
			If EventData() = 13
				val = Int( TextFieldText( tfdGRIDSIZE ) )
				If val < 1
					val = 4
					SetGadgetText tfdGRIDSIZE, val
				EndIf
				editor\grid\size = val
			EndIf

		Case chbLABELS
			editor\showlabels = ButtonState( chbLABELS )

		Case cobEXTENSION
			editor\fileextension = SelectedGadgetItem( cobEXTENSION )

		Case chbMAINLOOP
			editor\exportloop = ButtonState( chbMAINLOOP )

	End Select

	UpdateCanvas(DISPLAYCANVAS)

End Function

Function DoMouseAction( canvas )

	; takes action according to mode.. we moved mouse over canvas

	Select canvas
		Case DISPLAYCANVAS

			Local parent.gtype, gt.gtype
			Local xmoved, ymoved
			Local move = True

			; determine amount moved
			xmoved = MouseX( DISPLAYCANVAS ) - editor\grid\cx
			ymoved = MouseY( DISPLAYCANVAS ) - editor\grid\cy

			; store this new pos
			editor\grid\cx = MouseX( DISPLAYCANVAS )
			editor\grid\cy = MouseY( DISPLAYCANVAS )

			; check exceptions

			Select editor\mode

				Case MODE_MOVE
					If editor\selection\gt\version = FOLDBOX1 Then Return	; don't move foldboxes

				Case MODE_MOVEBOTTOM, MODE_BOX, MODE_NORMAL					; no exceptions in these modes

				Default

					If editor\selection <> Null
						If editor\selection\gt\version = FOLDBOX1 Then Return	; only scale height of foldboxes
					EndIf

			End Select

			; take action according to mode

			Select editor\mode

				Case MODE_NORMAL

					; the editor is in normal mode

					; see if we are over the corner of a selected gadget, and show
					; the correct movement pointer

					l.list = editor\selection

					While l <> Null
						If CornerHit( l\gt )
							Select editor\sidehandle
								Case MODE_MOVETL
									api_SetCursor(api_LoadCursor(0, 32642))
								Case MODE_MOVETR
									api_SetCursor(api_LoadCursor(0, 32643))
								Case MODE_MOVEBL
									api_SetCursor(api_LoadCursor(0, 32643))
								Case MODE_MOVEBR
									api_SetCursor(api_LoadCursor(0, 32642))
								Case MODE_MOVELEFT
									api_SetCursor(api_LoadCursor(0, 32644))
								Case MODE_MOVERIGHT
									api_SetCursor(api_LoadCursor(0, 32644))
								Case MODE_MOVETOP
									api_SetCursor(api_LoadCursor(0, 32645))
								Case MODE_MOVEBOTTOM
									api_SetCursor(api_LoadCursor(0, 32645))
							End Select

							Return
						EndIf
						l = l\nxt
					Wend

					; still here?
					; do we hover over a not selected gadget? If yes, show move pointer
					; start at end of childlist so we check the gadgets on top first

					gt.gtype = Last gtype

					While gt <> Null
						If GadgetHit( MouseX(canvas), MouseY(canvas), gt )
							If gt\version = TABPANEL Then Return	; don't react to these
							api_SetCursor(api_LoadCursor(0, 32646))
							Return
						EndIf
						gt = Before gt
					Wend

				Case MODE_BOX
				 	; see if our box contains any of the 4 points of a gadget. if yes, select it
				 	; gadgets which cannto be selected are cathed in that function

				 	For gt.gtype = Each gtype
					 	If GadgetInBox( gt ) Then LL_AddItem( gt, editor\selection )
				 	Next

				Case MODE_MOVE
				 	MoveGadgets( xmoved, ymoved, editor\selection )

				Case MODE_MOVETL			; topleft

					gt = editor\selection\gt
					parent = gt\parent

					If parent\version = TABPANEL Then parent = parent\parent

					; check if the topleft can move

					If editor\constraint
						move = PointInRect( gt\xpos+xmoved, gt\ypos+ymoved, parent\xpos,parent\ypos, parent\width, parent\height )
					EndIf

					If move = True

						; prevent gadget wrap

						If gt\xpos+xmoved < gt\xpos+gt\width - editor\grid\size
							gt\xpos = gt\xpos + xmoved
							gt\width = gt\width - xmoved
							MoveGadgets( xmoved, 0, gt\childlist )

							; if we scale a panel, check childlist and scale any foldboxes as well.
							If gt\version = PANEL Then ScaleFoldBoxes( gt\childlist )

						EndIf

						If gt\ypos+ymoved < gt\ypos+gt\height - editor\grid\size
							gt\ypos = gt\ypos + ymoved
							gt\height= gt\height - ymoved
							MoveGadgets( 0, ymoved, gt\childlist )
						EndIf
					EndIf

				Case MODE_MOVETR			; top right

					gt = editor\selection\gt
					parent = gt\parent
					If parent\version = TABPANEL Then parent = parent\parent

					If editor\constraint
						move = PointInRect( gt\xpos+gt\width+xmoved, gt\ypos+ymoved, parent\xpos,parent\ypos, parent\width, parent\height )
					EndIf

					If move = True

						; prevent gadget wrap

						gt\width = gt\width + xmoved
						If gt\width < editor\grid\size Then gt\width = editor\grid\size

						; if we scale a panel, check childlist and scale any folboxes as well.
						If gt\version = PANEL Then ScaleFoldBoxes( gt\childlist )

						If gt\ypos+ymoved < gt\ypos+gt\height - editor\grid\size
							gt\ypos = gt\ypos + ymoved
							gt\height= gt\height - ymoved
							MoveGadgets( 0, ymoved, gt\childlist )
						EndIf
					EndIf

				Case MODE_MOVEBL

					gt = editor\selection\gt
					parent = gt\parent
					If parent\version = TABPANEL Then parent = parent\parent

					If editor\constraint
						move = PointInRect( gt\xpos+xmoved, gt\ypos+gt\height+ymoved, parent\xpos,parent\ypos, parent\width, parent\height )
					EndIf

					If move = True

						If gt\xpos+xmoved < gt\xpos + gt\width - editor\grid\size
							gt\xpos = gt\xpos + xmoved
							gt\width = gt\width - xmoved
							MoveGadgets( xmoved, 0, gt\childlist )

							; if we scale a panel, check childlist and scale any foldboxes as well.

							If gt\version = PANEL Then ScaleFoldBoxes( gt\childlist )

						EndIf

						If gt\ypos+gt\height+ymoved > gt\ypos + editor\grid\size
							gt\height = gt\height + ymoved
						EndIf
					EndIf

				Case MODE_MOVEBR

					gt = editor\selection\gt
					parent = gt\parent
					If parent\version = TABPANEL Then parent = parent\parent

					If editor\constraint
						move = PointInRect( gt\xpos+gt\width+xmoved, gt\ypos+gt\height+ymoved, parent\xpos,parent\ypos, parent\width, parent\height )
					EndIf

					If move = True
						gt\width = gt\width + xmoved
						gt\height = gt\height + ymoved

						; prevent gadget wrap
						If gt\width < editor\grid\size Then gt\width = editor\grid\size
						If gt\height < editor\grid\size Then gt\height = editor\grid\size

						; if we scale a panel, check childlist and scale any foldboxes as well.

						If gt\version = PANEL Then ScaleFoldBoxes( gt\childlist )

					EndIf

				Case MODE_MOVELEFT

					gt = editor\selection\gt
					parent = gt\parent
					If parent\version = TABPANEL Then parent = parent\parent

					; prevent gadget wrap

					If gt\xpos+xmoved < gt\xpos+gt\width - editor\grid\size
						gt\xpos = gt\xpos + xmoved
						gt\width = gt\width - xmoved

						; if needed, don't let side leave parent

						If editor\constraint
							If gt\xpos < parent\xpos
								gt\xpos = parent\xpos
								gt\width = gt\width + xmoved
								xmoved = 0
							EndIf
						EndIf

						; if we scale a panel, check childlist and scale any foldboxes as well.

						If gt\version = PANEL Then ScaleFoldBoxes( gt\childlist )

						MoveGadgets( xmoved, 0, gt\childlist )

					EndIf

				Case MODE_MOVERIGHT

					gt = editor\selection\gt
					parent = gt\parent
					If parent\version = TABPANEL Then parent = parent\parent

					; prevent gadget wrap

					If gt\xpos+gt\width+xmoved > gt\xpos + editor\grid\size
						gt\width = gt\width + xmoved

						; if needed, don't let side leave parent

						If editor\constraint
							If gt\xpos+gt\width+xmoved > parent\xpos+parent\width
								gt\width = gt\width - xmoved
							EndIf
						EndIf

						; if we scale a panel, check childlist and scale any foldboxes as well.

						If gt\version = PANEL Then ScaleFoldBoxes( gt\childlist )

					EndIf

				Case MODE_MOVETOP

					gt = editor\selection\gt
					parent = gt\parent
					If parent\version = TABPANEL Then parent = parent\parent

					; prevent gadget wrap

					If gt\ypos+ymoved < gt\ypos+gt\height - editor\grid\size
						gt\ypos = gt\ypos + ymoved
						gt\height = gt\height - ymoved

						; if needed, don't let side leave parent

						If editor\constraint
							If gt\ypos < parent\ypos
								gt\ypos = gt\ypos - ymoved
								gt\height = gt\height + ymoved
								ymoved = 0
							EndIf
						EndIf

						MoveGadgets( 0, ymoved, gt\childlist )

					EndIf

				Case MODE_MOVEBOTTOM

					gt = editor\selection\gt
					parent = gt\parent
					If parent\version = TABPANEL Then parent = parent\parent

					; prevent gadget wrap

					If gt\ypos+gt\height+ymoved > gt\ypos + editor\grid\size
						gt\height = gt\height + ymoved

						; if needed, don't let side leave parent

						If editor\constraint
							If gt\ypos+gt\height > parent\ypos+parent\height
								gt\height = gt\height - ymoved
							EndIf
						EndIf

					EndIf

					; do we move the bottom of a foldbox? if yes, move any boxes below it

					If gt\version = FOLDBOX1

						child.list = gt\parent\childlist

						; move foldboxes and its childlists

						While child\nxt <> Null
							nextypos = child\gt\ypos + child\gt\height
							ymoved = nextypos - child\nxt\gt\ypos

							child = child\nxt

							child\gt\ypos = nextypos
							MoveGadgets( 0, ymoved, child\gt\childlist, True )
						Wend

					EndIf

			End Select

	End Select

	UpdateCanvas(canvas)

End Function

Function SnapPosToGrid( xpos#, ypos# )

	; snaps passed xy pos to grid coordinates. results are storing in globals
	; find nearest grid position

	Local nearx = xpos / editor\grid\size
	Local neary = ypos / editor\grid\size
	GR_RESULTX = nearx * editor\grid\size
	GR_RESULTY = neary * editor\grid\size

End Function

Function ResizeWindow()

	; we have resized edit window. re-create canvasses

	Local menuoffset = 0
	FreeGadget DISPLAYCANVAS
	If editor\rootgt\menu = True Then menuoffset = 20		; make canvas smaller
	DISPLAYCANVAS = CreateCanvas(0,0, ClientWidth(EDITWINDOW), ClientHeight(EDITWINDOW)-menuoffset, EDITWINDOW)
	SetGadgetLayout DISPLAYCANVAS, 1, 0, 1, 0
	UpdateCanvas(DISPLAYCANVAS)

	If editor\rootgt\menu = True

		; do menu canvas as well
		FreeGadget MENUCANVAS
		MENUCANVAS = CreateCanvas( 0,0, ClientWidth( EDITWINDOW ), 20, EDITWINDOW )
		SetGadgetLayout MENUCANVAS, 1, 1, 1,0
		SetGadgetShape DISPLAYCANVAS, 0,19, GadgetWidth( DISPLAYCANVAS ) , GadgetHeight( DISPLAYCANVAS )
		UpdateCanvas(MENUCANVAS)
	EndIf

	editor\mode = MODE_NORMAL

	; pass new size to edit windows gadget type
;	editor\rootGT\width = GadgetWidth( DISPLAYCANVAS )
;	editor\rootGT\height = GadgetHeight( DISPLAYCANVAS )

	; only detect change if there are gadgets on the window
	If editor\rootgt\childlist <> Null Then editor\changed = True
End Function

Function CreatePropInput( item )

	; show the correct input gadget for the selected gadget and property

	HideInputGadgets()
	ClearGadgetItems PROPCOMBO
	SetStatusText MAINWINDOW, ""

	; store selected item
	editor\activeprop = item
	
	Local FREE$, LOCKED$, FLOATS$

	Local gt.gtype = editor\selection\gt

	; generate correct wording of gadget properties depending on language selected
	
	Select SelectedGadgetItem(EXPORT_TYPE)
		Case 0
			;bplus selected
			FREE$ = "Free"
			LOCKED$ = "Locked"
			FLOATS$ = "Float"
		Case 1
			;bmax selected
			FREE$ = "Centered"
			LOCKED$ = "Aligned"
			FLOATS$ = "Relative"
	End Select

	Select gt\version

		Case WINDOW
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\label$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					SetGadgetText PROPSELECT, "Titlebar"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\titlebar
				Case 7
					SetGadgetText PROPSELECT, "Resizeable"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\resize
				Case 8
					SetGadgetText PROPSELECT, "Menu"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\menu
					SetStatusText MAINWINDOW, "Set this to add a menu to the window"
				Case 9
					SetGadgetText PROPSELECT, "Statusbar"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\statusbar
				Case 10
					SetGadgetText PROPSELECT, "Tool window"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\tool
				Case 11
					SetGadgetText PROPSELECT, "Client coordinates"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\client

			End Select

;		Case SPLITTER
;			Select item
;				Case 0
;					SetGadgetText PROPTEXT, gt\name$
;					ActivateGadget PROPTEXT
;					ShowGadget PROPTEXT
;					SelectAllText( PROPTEXT )
;				Case 1
;					SetGadgetText PROPTEXT, Int(gt\xpos)
;					ActivateGadget PROPTEXT
;					ShowGadget PROPTEXT
;					SelectAllText( PROPTEXT )
;				Case 2
;					SetGadgetText PROPTEXT, Int(gt\ypos)
;					ActivateGadget PROPTEXT
;					ShowGadget PROPTEXT
;					SelectAllText( PROPTEXT )
;				Case 3
;					SetGadgetText PROPTEXT, gt\width
;					ActivateGadget PROPTEXT
;					ShowGadget PROPTEXT
;					SelectAllText( PROPTEXT )
;				Case 4
;					SetGadgetText PROPTEXT, gt\height
;					ActivateGadget PROPTEXT
;					ShowGadget PROPTEXT
;					SelectAllText( PROPTEXT )
;				Case 5
;					AddGadgetItem PROPCOMBO, "Horizontal"
;					AddGadgetItem PROPCOMBO, "Vertical"
;					SelectGadgetItem PROPCOMBO, gt\direction
;					ShowGadget PROPCOMBO
;				Case 6
;					SetGadgetText PROPSELECT, "Enabled"
;					SetButtonState PROPSELECT, gt\enable
;					ShowGadget PROPSELECT
;				Case 7
;					ShowGadget PROPTEXT
;					SetGadgetText PROPTEXT, gt\group$
;					ActivateGadget PROPTEXT
;					SelectAllText( PROPTEXT )
;				Case 8
;					AddGadgetItem PROPCOMBO, "Free"
;					AddGadgetItem PROPCOMBO, "Locked"
;					AddGadgetItem PROPCOMBO, "Float"
;					SelectGadgetItem PROPCOMBO, gt\layoutleft
;					ShowGadget PROPCOMBO
;				Case 9
;					AddGadgetItem PROPCOMBO, "Free"
;					AddGadgetItem PROPCOMBO, "Locked"
;					AddGadgetItem PROPCOMBO, "Float"
;					SelectGadgetItem PROPCOMBO, gt\layoutright
;					ShowGadget PROPCOMBO
;				Case 10
;					AddGadgetItem PROPCOMBO, "Free"
;					AddGadgetItem PROPCOMBO, "Locked"
;					AddGadgetItem PROPCOMBO, "Float"
;					SelectGadgetItem PROPCOMBO, gt\layouttop
;					ShowGadget PROPCOMBO
;				Case 11
;					AddGadgetItem PROPCOMBO, "Free"
;					AddGadgetItem PROPCOMBO, "Locked"
;					AddGadgetItem PROPCOMBO, "Float"
;					SelectGadgetItem PROPCOMBO, gt\layoutbottom
;					ShowGadget PROPCOMBO
;
;			End Select

		Case COLSELECTOR
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PANELCOL
				Case 6
					SetGadgetText PROPSELECT, "Enabled"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\enable
				Case 7
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 8
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

;			SetGadgetText VERSIONLABEL, "Type: ColorSelector" + append$
;			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
;			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
;			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
;			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
;			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
;			AddGadgetItem GADGETPROPBOX, "Default Color: " + gt\r + "," + gt\g + "," + gt\b	;5
;			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable			;6
;			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$		;7



		Case IMAGE
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PANELCOL
				Case 6
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 7
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 8
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case SPINNER
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\defaultstring$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					AddGadgetItem PROPCOMBO, "Float"
					AddGadgetItem PROPCOMBO, "Int"
					SelectGadgetItem PROPCOMBO, gt\integer
					ShowGadget PROPCOMBO
				Case 7
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\minval
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\maxval
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 9
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\stp
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 10
					SetGadgetText PROPSELECT, "Enabled"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\enable
				Case 11
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 12
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\autolabel$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 13
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\prefix$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 14
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\suffix$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 15
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 16
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 17
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 18
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO
			End Select

		Case FOLDBOX1
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\label$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
			End Select

		Case SEPLINE
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\label$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					SetGadgetText PROPSELECT, "Enabled"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\enable
				Case 7
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 8
					AddGadgetItem PROPCOMBO, "Horizontal"
					AddGadgetItem PROPCOMBO, "Vertical"
					SelectGadgetItem PROPCOMBO, gt\direction
					ShowGadget PROPCOMBO
					SetStatusText MAINWINDOW, "Sets line orientation"

			End Select

		Case BUTTON,GROUPBOX
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\label$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					SetGadgetText PROPSELECT, "Enabled"
					ShowGadget PROPSELECT
					SetButtonState PROPSELECT, gt\enable
				Case 7
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 8
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case LABEL
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\label$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					AddGadgetItem PROPCOMBO, "None"
					AddGadgetItem PROPCOMBO, "Flat"
					AddGadgetItem PROPCOMBO, "None"
					AddGadgetItem PROPCOMBO, "Sunken 3D"
					SelectGadgetItem PROPCOMBO, gt\border
					ShowGadget PROPCOMBO
					SetStatusText MAINWINDOW, "Sets border appearance"
				Case 7
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case TEXTFIELD
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					SetGadgetText PROPTEXT, gt\defaultstring$
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					SetGadgetText PROPSELECT, "Mask text"
					SetButtonState PROPSELECT, gt\mask
					ShowGadget PROPSELECT
					SetStatusText MAINWINDOW, "Set to only display *'s"
				Case 7
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 9
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\autolabel$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )

				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 13
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case TEXTAREA
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					SetGadgetText PROPTEXT, gt\defaultstring$
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					ShowGadget AREATXT
				Case 7
					ShowGadget AREACOL
					ShowGadget DEF
				Case 8
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 9
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 13
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case CHECKBOX,RADIOBUTTON
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\label$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					SetGadgetText PROPSELECT, "Checked"
					SetButtonState PROPSELECT, gt\checked
					ShowGadget PROPSELECT
				Case 7
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case COMBOBOX
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					SetGadgetText PROPTEXT, gt\items$
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
					SetStatusText MAINWINDOW, "Use ; to seperate items"
				Case 6
					For count = 0 To gt\itemcount
						AddGadgetItem PROPCOMBO, count
					Next
					SelectGadgetItem PROPCOMBO, gt\defaultitem
					ShowGadget PROPCOMBO
					SetStatusText MAINWINDOW, "Sets the default selected item"
				Case 7
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 9
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\autolabel$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )

				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 13
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case LISTBOX,TABBER
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					SetGadgetText PROPTEXT, gt\items$
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
					SetStatusText MAINWINDOW, "Use ; to seperate items"
				Case 6
					For count = 0 To gt\itemcount
						AddGadgetItem PROPCOMBO, count
					Next
					SelectGadgetItem PROPCOMBO, gt\defaultitem
					ShowGadget PROPCOMBO
					SetStatusText MAINWINDOW, "Sets the default selected item"
				Case 7
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case TREEVIEW
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 6
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 7
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 8
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case PROGRESSBAR
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\progressval
					ActivateGadget PROPTEXT
				Case 6
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 7
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\autolabel$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case HTML
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\defaulturl$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 6
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 7
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 8
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case STATUSBAR

		Case CANVAS
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					SetGadgetText PROPSELECT, "Show pointer"
					SetButtonState PROPSELECT, gt\pointershow
					ShowGadget PROPSELECT
					SetStatusText MAINWINDOW, "Uncheck this to hide mouse pointer"
				Case 6
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 7
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 8
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case WINDOW

		Case PANEL
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					SetGadgetText PROPSELECT, "Border"
					SetButtonState PROPSELECT, gt\border
					ShowGadget PROPSELECT
				Case 6
					ShowGadget PANELCOL
					ShowGadget DEF
				Case 7
					ShowGadget PANELIMG

				Case 8
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )

				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 12
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

		Case TABPANEL
			Select item
				Case 0
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					For count = -1 To gt\parent\itemcount
						AddGadgetItem PROPCOMBO, count
					Next
					SelectGadgetItem PROPCOMBO, gt\tabitem +1
					ShowGadget PROPCOMBO
					SetStatusText MAINWINDOW, "Sets the tabber-item this panel belongs to"

			End Select

		Case SLIDER, SPLITTER
			Select item
				Case 0
					SetGadgetText PROPTEXT, gt\name$
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 1
					SetGadgetText PROPTEXT, Int(gt\xpos)
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 2
					SetGadgetText PROPTEXT, Int(gt\ypos)
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 3
					SetGadgetText PROPTEXT, gt\width
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 4
					SetGadgetText PROPTEXT, gt\height
					ActivateGadget PROPTEXT
					ShowGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 5
					AddGadgetItem PROPCOMBO, "Horizontal"
					AddGadgetItem PROPCOMBO, "Vertical"
					SelectGadgetItem PROPCOMBO, gt\direction
					ShowGadget PROPCOMBO
				Case 6
					SetGadgetText PROPSELECT, "Enabled"
					SetButtonState PROPSELECT, gt\enable
					ShowGadget PROPSELECT
				Case 7
					ShowGadget PROPTEXT
					SetGadgetText PROPTEXT, gt\group$
					ActivateGadget PROPTEXT
					SelectAllText( PROPTEXT )
				Case 8
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutleft
					ShowGadget PROPCOMBO
				Case 9
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutright
					ShowGadget PROPCOMBO
				Case 10
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layouttop
					ShowGadget PROPCOMBO
				Case 11
					AddGadgetItem PROPCOMBO, FREE$
					AddGadgetItem PROPCOMBO, LOCKED$
					AddGadgetItem PROPCOMBO, FLOATS$
					SelectGadgetItem PROPCOMBO, gt\layoutbottom
					ShowGadget PROPCOMBO

			End Select

	End Select

End Function

Function StoreMenuInput()

	; stores the changed menu value

	Local mt.mtype = editor\mselection\mt

	; check special items for each gadget type

	Select mt\version
		Case MENUITEM
			Select editor\activeprop
				Case 0
					mt\label$ = TextFieldText( MENUTXT )
					ModifyTreeViewNode mt\node, mt\label$
					UpdateCanvas( MENUCANVAS )
				Case 1
					mt\active = ButtonState( MENUCHECK )
				Case 2
					mt\checkable = ButtonState( MENUCHECK )
				Case 3
					mt\checked = ButtonState( MENUCHECK )
;				Case 4
;					mt\shortcut = TextFieldText( MENUTXT )
;				Case 5
;					mt\shortcutmod = SelectedGadgetItem( MENUCOMBO )

			End Select
	End Select

	ShowMenuDetails( mt );, False )
	editor\changed = True

End Function

Function StorePropInput()

	; stores the value or item selected in the valuegadget item to the selected gadget

	Local gt.gtype = editor\selection\gt

	; check special items for each gadget type

	Select gt\version
		Case WINDOW
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\label$ = TextFieldText( PROPTEXT )
					SetGadgetText EDITWINDOW, gt\label$
				Case 2
					gt\xpos = TextFieldText( PROPTEXT )
				Case 3
					gt\ypos = TextFieldText( PROPTEXT )
				Case 4
					gt\width = TextFieldText(PROPTEXT)
					SetGadgetShape(EDITWINDOW, GadgetX(EDITWINDOW), GadgetY(EDITWINDOW), gt\width, gt\height)
					ResizeWindow()
				Case 5
					gt\height = TextFieldText(PROPTEXT)
					SetGadgetShape(EDITWINDOW, GadgetX(EDITWINDOW), GadgetY(EDITWINDOW), gt\width, gt\height)
					ResizeWindow()
				Case 6
					gt\titlebar = ButtonState(PROPSELECT )
				Case 7
					gt\resize = ButtonState(PROPSELECT )
				Case 8
					gt\menu = ButtonState(PROPSELECT )
					If gt\menu = True
						SetGadgetShape DISPLAYCANVAS, 0,19, GadgetWidth( DISPLAYCANVAS) , GadgetHeight( DISPLAYCANVAS )
						ShowGadget MENUCANVAS
						UpdateCanvas( MENUCANVAS )
					Else
						SetGadgetShape DISPLAYCANVAS, 0,0, GadgetWidth( DISPLAYCANVAS) , GadgetHeight( DISPLAYCANVAS )
						HideGadget MENUCANVAS
					EndIf
				Case 9
					gt\statusbar = ButtonState( PROPSELECT )
				Case 10
					gt\tool = ButtonState( PROPSELECT )
				Case 11
					gt\client = ButtonState( PROPSELECT )
			End Select

		Case COLSELECTOR
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 6
					gt\enable = ButtonState( PROPSELECT )
				Case 7
					gt\group$ = TextFieldText( PROPTEXT )
				Case 8
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 9
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case IMAGE
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )

				Case 6
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 7
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 8
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 9
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case SPINNER
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\integer = SelectedGadgetItem( PROPCOMBO )
					If gt\integer
						gt\defaultstring$ = Str$( Int( gt\defaultstring$) )
					Else
						gt\defaultstring$ = Str$ (Float( gt\defaultstring$) )
					EndIf
				Case 6
					gt\defaultstring$ = TextFieldText( PROPTEXT )
				Case 7
					gt\minval = TextFieldText( PROPTEXT )
				Case 8
					gt\maxval = TextFieldText( PROPTEXT )
				Case 9
					gt\stp = TextFieldText( PROPTEXT )
				Case 10
					gt\enable = ButtonState( PROPSELECT )
				Case 11
					gt\group$ = TextFieldText( PROPTEXT )
				Case 12
					gt\autolabel$ = TextFieldText( PROPTEXT )
				Case 13
					gt\prefix$ = TextFieldText( PROPTEXT )
				Case 14
					gt\suffix$ = TextFieldText( PROPTEXT )
				Case 15
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 16
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 17
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 18
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case FOLDBOX1
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\label$ = TextFieldText( PROPTEXT )
				Case 2
					gt\height = TextFieldText( PROPTEXT )
				Case 3
					gt\group$ = TextFieldText( PROPTEXT )
			End Select

		Case SEPLINE
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\label$ = TextFieldText( PROPTEXT )
				Case 2
					gt\xpos = TextFieldText( PROPTEXT )
				Case 3
					gt\ypos = TextFieldText( PROPTEXT )
				Case 4
					gt\width = TextFieldText( PROPTEXT )
				Case 5
					gt\height = TextFieldText( PROPTEXT )
				Case 6
					gt\enable = ButtonState( PROPSELECT )
				Case 7
					gt\group$ = TextFieldText( PROPTEXT )
				Case 8
					gt\direction = SelectedGadgetItem( PROPCOMBO )

					If gt\direction = 0
						gt\width = 64
						gt\height = 16
					Else
						gt\width = 16
						gt\height = 64
					EndIf

			End Select

		Case BUTTON,GROUPBOX
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\label$ = TextFieldText( PROPTEXT )
				Case 2
					xmove = Int( TextFieldText( PROPTEXT ) ) - gt\xpos
					gt\xpos = TextFieldText( PROPTEXT )
					If gt\version = GROUPBOX Then MoveGadgets( xmove, 0, gt\childlist,True )
				Case 3
					ymove = Int( TextFieldText( PROPTEXT ) ) - gt\ypos
					gt\ypos = TextFieldText( PROPTEXT )
					If gt\version = GROUPBOX Then MoveGadgets( 0,ymove, gt\childlist,True )
				Case 4
					gt\width = TextFieldText( PROPTEXT )
				Case 5
					gt\height = TextFieldText( PROPTEXT )
				Case 6
					gt\enable = ButtonState( PROPSELECT )
				Case 7
					gt\group$ = TextFieldText( PROPTEXT )
				Case 8
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 9
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case LABEL
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\label$ = TextFieldText( PROPTEXT )

					; check size of label and re-size the gadget if necessary
					strlength = StringWidth( gt\label$ )
					If gt\width < strlength Then gt\width = strlength
				Case 2
					gt\xpos = TextFieldText( PROPTEXT )
				Case 3
					gt\ypos = TextFieldText( PROPTEXT )
				Case 4
					gt\width = TextFieldText( PROPTEXT )
				Case 5
					gt\height = TextFieldText( PROPTEXT )
				Case 6
					gt\border = SelectedGadgetItem( PROPCOMBO )
				Case 7
					gt\enable = ButtonState( PROPSELECT )
				Case 8
					gt\group$ = TextFieldText( PROPTEXT )
				Case 9
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case TEXTFIELD
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\defaultstring$ = TextFieldText( PROPTEXT )
				Case 6
					gt\mask = ButtonState( PROPSELECT )
				Case 7
					gt\enable = ButtonState( PROPSELECT )
				Case 8
					gt\group$ = TextFieldText( PROPTEXT )
				Case 9
					gt\autolabel$ = TextFieldText( PROPTEXT )
				Case 10
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 13
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case TEXTAREA
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\defaultstring$ = TextFieldText( PROPTEXT )
				Case 8
					gt\enable = ButtonState( PROPSELECT )
				Case 9
					gt\group$ = TextFieldText( PROPTEXT )
				Case 10
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 13
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case CHECKBOX, RADIOBUTTON
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\label$ = TextFieldText( PROPTEXT )

					; check size of label and re-size the gadget if necessary
					strlength = StringWidth( gt\label$ ) + 16
					If gt\width < strlength Then gt\width = strlength
				Case 2
					gt\xpos = TextFieldText( PROPTEXT )
				Case 3
					gt\ypos = TextFieldText( PROPTEXT )
				Case 4
					gt\width = TextFieldText( PROPTEXT )
				Case 5
					gt\height = TextFieldText( PROPTEXT )
				Case 6
					gt\checked = ButtonState( PROPSELECT )
				Case 7
					gt\enable = ButtonState( PROPSELECT )
				Case 8
					gt\group$ = TextFieldText( PROPTEXT )
				Case 9
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case TREEVIEW
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\enable = ButtonState( PROPSELECT )
				Case 6
					gt\group$ = TextFieldText( PROPTEXT )
				Case 7
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 8
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 9
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case COMBOBOX
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					xmove = Int( TextFieldText( PROPTEXT ) ) - gt\xpos
					gt\xpos = TextFieldText( PROPTEXT )
					If gt\version = TABBER Then MoveGadgets( xmove, 0, gt\childlist, True )
				Case 2
					ymove = Int( TextFieldText( PROPTEXT ) ) - gt\ypos
					gt\ypos = TextFieldText( PROPTEXT )
					If gt\version = TABBER Then MoveGadgets( 0,ymove, gt\childlist, True )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					If TextFieldText( PROPTEXT ) <> "" Then gt\items$ = TextFieldText( PROPTEXT )
					gt\itemcount = CountItems( gt\items$ )
					If gt\version = TABBER Then ManageTabber( gt )
				Case 6
					gt\defaultitem = SelectedGadgetItem( PROPCOMBO )
				Case 7
					gt\enable = ButtonState( PROPSELECT )
				Case 8
					gt\group$ = TextFieldText( PROPTEXT )
				Case 9
					gt\autolabel$ = TextFieldText( PROPTEXT )
				Case 10
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 13
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case LISTBOX,TABBER
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					xmove = Int( TextFieldText( PROPTEXT ) ) - gt\xpos
					gt\xpos = TextFieldText( PROPTEXT )
					If gt\version = TABBER Then MoveGadgets( xmove, 0, gt\childlist, True )
				Case 2
					ymove = Int( TextFieldText( PROPTEXT ) ) - gt\ypos
					gt\ypos = TextFieldText( PROPTEXT )
					If gt\version = TABBER Then MoveGadgets( 0,ymove, gt\childlist, True )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					If TextFieldText(PROPTEXT) <> "" Then gt\items$ = TextFieldText(PROPTEXT)
					If gt\version = TABBER Then ManageTabber( gt )
				Case 6
					gt\defaultitem = SelectedGadgetItem( PROPCOMBO )
				Case 7
					gt\enable = ButtonState( PROPSELECT )
				Case 8
					gt\group$ = TextFieldText( PROPTEXT )
				Case 9
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case PROGRESSBAR
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\progressval = TextFieldText( PROPTEXT )
				Case 6
					gt\enable = ButtonState( PROPSELECT )
				Case 7
					gt\group$ = TextFieldText( PROPTEXT )
				Case 8
					gt\autolabel$ = TextFieldText( PROPTEXT )
				Case 9
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case HTML
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\defaulturl = TextFieldText( PROPTEXT )
				Case 6
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 7
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 8
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 9
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case CANVAS
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					xmove = Int( TextFieldText( PROPTEXT ) ) - gt\xpos
					gt\xpos = TextFieldText( PROPTEXT )
					MoveGadgets( xmove, 0, gt\childlist, True )
				Case 2
					ymove = Int( TextFieldText( PROPTEXT ) ) - gt\ypos
					gt\ypos = TextFieldText( PROPTEXT )
					MoveGadgets( 0,ymove, gt\childlist, True )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\pointershow = ButtonState( PROPSELECT )
				Case 6
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 7
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 8
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 9
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case PANEL
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					xmove = Int( TextFieldText( PROPTEXT ) ) - gt\xpos
					gt\xpos = TextFieldText( PROPTEXT )
					MoveGadgets( xmove, 0, gt\childlist, True )
				Case 2
				ymove = Int( TextFieldText( PROPTEXT ) ) - gt\ypos
					gt\ypos = TextFieldText( PROPTEXT )
					MoveGadgets( 0,ymove, gt\childlist, True )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\border = ButtonState( PROPSELECT )

				Case 8
					gt\group$ = TextFieldText( PROPTEXT )
				Case 9
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 12
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

		Case TABPANEL
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\tabitem = SelectedGadgetItem( PROPCOMBO ) -1
			End Select

		Case SLIDER,SPLITTER
			Select editor\activeprop
				Case 0
					gt\name$ = TextFieldText( PROPTEXT )
					ModifyTreeViewNode gt\node, gt\name$
				Case 1
					gt\xpos = TextFieldText( PROPTEXT )
				Case 2
					gt\ypos = TextFieldText( PROPTEXT )
				Case 3
					gt\width = TextFieldText( PROPTEXT )
				Case 4
					gt\height = TextFieldText( PROPTEXT )
				Case 5
					gt\direction = SelectedGadgetItem( PROPCOMBO )

					; resize
					If gt\version = SPLITTER
						If gt\direction = 0
							gt\width = 64
							gt\height = 8
						Else
							gt\width = 8
							gt\height = 64
						EndIf
					Else
						If gt\direction = 0
							gt\width = 64
							gt\height = 16
						Else
							gt\width = 16
							gt\height = 64
						EndIf
					EndIf
				Case 6
					gt\enable = ButtonState( PROPSELECT )
				Case 7
					gt\group$ = TextFieldText( PROPTEXT )
				Case 8
					gt\layoutleft = SelectedGadgetItem( PROPCOMBO )
				Case 9
					gt\layoutright = SelectedGadgetItem( PROPCOMBO )
				Case 10
					gt\layouttop = SelectedGadgetItem( PROPCOMBO )
				Case 11
					gt\layoutbottom = SelectedGadgetItem( PROPCOMBO )
			End Select

	End Select

	ShowGadgetDetails( gt, False )
	editor\changed = True
End Function

Function ProjectNew()

	; starts a new project
	Local ok = True
	If editor\changed = True
		ok = Proceed( "Do you want to save changes first?", 1 )
		If ok = -1 Then Return					; return to guide if user pressed Cancel
		If ok = 1 Then ProjectSave()			; save first if user pressed Yes
		ok = 1
	EndIf

	; get rid of old project
	For gt.gtype = Each gtype
		If gt <> editor\rootgt Then Delete gt
	Next

	editor\rootgt\childlist = Null
	Delete Each list
	editor\selection = Null

	; reset settings
	gt.gtype = editor\rootgt
	gt\name$ = "EditWindow"
	gt\label$ = gt\name$
	gt\parent = Null
	gt\xpos = 0
	gt\ypos = 0
	gt\menu = False
	gt\resize = True
	gt\statusbar = False
	gt\titlebar = True
	gt\tool = False
	gt\client = False

	ResetGadgetCounters()
	SetGadgetText EDITWINDOW, gt\label$
	FreeTreeViewNode editor\rootgt\node
	gt\node = AddTreeViewNode( gt\name$, TreeViewRoot( GADGETTREE ), WINDOW )
	SetGadgetLayout MENUCANVAS, 1, 1, 1,0
	HideGadget MENUCANVAS

	; get rid of menu items
	Delete Each mtype
	editor\mselection = Null

	; create root menu item
	editor\rootmt = New mtype

	; add menu node to editwin node
	editor\rootmt\node = AddTreeViewNode( "MENUBAR", editor\rootgt\node, MENUROOT )
	editor\rootmt\version = MENUROOT
	editor\rootmt\label$ = "MENU"

	; show it
	ExpandTreeViewNode gt\node

	HideInputGadgets()
	ClearGadgetItems PROPCOMBO
	SetStatusText MAINWINDOW, ""
	editor\changed = False
	Return ok
End Function

Function ProjectLoad()

	; loads a gui project

	Local stream
	Local filename$, version$

	; see if we need to save changes
	Local ok% = ProjectNew()

	If ok

		; ask name
		filename$ = RequestFile( "Load GUIde project","gup",0 )

		If filename$ <> ""
			stream = ReadFile( filename$ )
			If stream
				editor\loadedproject$ = filename$
				ResetGadgetCounters()

				gt.gtype = editor\rootgt

				; load settings

				; get saved version
				version$ = ReadString( stream )

				; get settings of main window
				gt\name$ = ReadString( stream )
				gt\width = ReadInt( stream )
				gt\height = ReadInt( stream )
				gt\label$ = ReadString( stream )

				gt\titlebar = ReadByte( stream )
				gt\resize = ReadByte( stream )
				gt\menu = ReadByte( stream )
				gt\statusbar = ReadByte( stream )
				gt\tool = ReadByte( stream )
				gt\client = ReadByte( stream )

				gt\xpos = 0
				gt\ypos = 0

				;rename main window
				SetGadgetText MAINWINDOW, APP$ + " - " + editor\loadedproject$

				; rename treeview root
				ModifyTreeViewNode gt\node, gt\name$

				; reset editwindow
				SetGadgetText EDITWINDOW, gt\label$

				SetGadgetShape EDITWINDOW, GadgetX( EDITWINDOW ), GadgetY( EDITWINDOW ), gt\width, gt\height
				ResizeWindow()

				LoadGadget( stream, gt, version$ )
				LoadMenuItem( stream, editor\rootmt, version$ )

				; show full tree
				ExpandTreeViewNode gt\node
				ExpandTreeViewNode editor\rootmt\node

				; de-select all

				HideInputGadgets()
				SetGadgetText VERSIONLABEL, "Type:"
				ClearGadgetItems GADGETPROPBOX
				SetStatusText MAINWINDOW, ""

				LL_ClearList( editor\selection )
				LL_ClearMenuList( editor\mselection )

				CloseFile stream

			EndIf

			editor\changed = False

			UpdateCanvas( DISPLAYCANVAS )
			UpdateCanvas( MENUCANVAS )
		EndIf
	EndIf

End Function

Function LoadMenuItem( stream, parent.mtype, version$ )

	; loads menu item and adds it to parents' childlist
	; differences in release versions are caught too

	Local mt.mtype

	Local nxt = ReadByte( stream )

	While nxt = 2		; then a menuitem follows

		mt = New mtype

		mt\parent = parent

		mt\version = ReadByte( stream )
		mt\active = ReadByte( stream )
		mt\checkable = ReadByte( stream )
		mt\checked = ReadByte( stream )
		mt\id = ReadByte( stream )
		mt\label$ = ReadString( stream )
		mt\shortcut$ = ReadString( stream )
		mt\shortcutmod = ReadByte( stream )

		; !!! version changes go here !!!

		Select version$
;			Case "1.0.2"
				; add mtype fields from prev version here , and the new fields after that
		End Select

		; add gadget to the treeview
		mt\node = AddTreeViewNode( mt\label$, parent\node, MENUROOT )

		; update counter
		editor\menuidcount = editor\menuidcount + 1

		; add gadget to parent childlist
		LL_AddItemMenu( mt, parent\childlist, LL_CHILD )

		nxt = ReadByte( stream )
		If nxt = 1
			LoadMenuItem( stream, mt, version$ ) 	; new childlist follows. this gadget is the parent
			nxt = ReadByte( stream )				; after returning here, check if there is a next gadget
		EndIf

	Wend

End Function

Function ExportCode()

	; exports the gadgets in code

	Local style
	Local filename$

	; get gadget handle prefix string

	If editor\prefix$ <> ""
		If Right$( editor\prefix$, 1 ) <> " " Then editor\prefix$ = editor\prefix$ + " "
	EndIf

	; reset indention

	editor\indentlevel = 0

	; ask file name

	Select SelectedGadgetItem(EXPORT_TYPE)
		Case 0
			filename$ = RequestFile( "Export B+ GUI code", "bb", 1 )
		Case 1
			filename$ = RequestFile( "Export Bmax GUI code", "bmx", 1 )
	End Select

	If filename$ <> ""
		stream = WriteFile( filename$ )
		If stream
			Select SelectedGadgetItem(EXPORT_TYPE)
				Case 0
					StartBlitzPlusExport( stream )
				Case 1
					StartBlitzMAXExport( stream )
			End Select
			CloseFile stream
		Else
			Notify "unable to open export gui code!", 1
		EndIf
	EndIf

End Function

Function ProjectSave( quick=False )

	; saves a project file
	Local stream
	Local filename$

	If quick = True
		filename$ = editor\loadedproject$
		If filename$ = "" Then filename$ = RequestFile( "Save GUIde project","gup",1 )
	Else
		filename$ = RequestFile( "Save GUIde project","gup",1 )
	EndIf

	If filename$ <> ""
		stream = WriteFile( filename$ )
		If stream
			editor\loadedproject$ = filename$

			; save version number
			WriteString stream, APPVERSION$

			; save main window gadget
			gt.gtype = editor\rootgt
			WriteString stream, gt\name$
			WriteInt stream, GadgetWidth(EDITWINDOW)  ;gt\width
			WriteInt stream, GadgetHeight(EDITWINDOW) ;gt\height
			WriteString stream, gt\label$
			WriteByte stream, gt\titlebar
			WriteByte stream, gt\resize
			WriteByte stream, gt\menu
			WriteByte stream, gt\statusbar
			WriteByte stream, gt\tool
			WriteByte stream, gt\client

			SaveGadgetList(gt\childlist, stream)
			SaveMenuList(editor\rootmt\childlist, stream)

			CloseFile stream
			editor\changed = False
			SetStatusText MAINWINDOW, "Project saved..."
		EndIf
	EndIf

End Function

Function SaveGadgetList( l.list, stream )

	; saves the gadgets in the passed list
	; all gtype fields are saved cos i am lazy

	While l <> Null
		WriteByte stream, 2							; a gadget follows
		gt.gtype = l\gt
		WriteByte stream, gt\version
		WriteByte stream, gt\hide
		WriteString stream, gt\name$
		WriteByte stream, gt\enable
		WriteInt stream, gt\xpos
		WriteInt stream, gt\ypos
		WriteInt stream, gt\width
		WriteInt stream, gt\height
		WriteString stream, gt\label$
		WriteString stream, gt\items$
		WriteString stream, gt\defaultstring$
		WriteByte stream, gt\r
		WriteByte stream, gt\g
		WriteByte stream, gt\b
		WriteByte stream, gt\textr
		WriteByte stream, gt\textg
		WriteByte stream, gt\textb
		WriteByte stream, gt\layoutleft
		WriteByte stream, gt\layoutright
		WriteByte stream, gt\layouttop
		WriteByte stream, gt\layoutbottom
		WriteString stream, gt\image$
		WriteByte stream, gt\pointershow
		WriteByte stream, gt\checked
		WriteByte stream, gt\mask
		WriteByte stream, gt\wordwrap
		WriteByte stream, gt\direction
		WriteFloat stream, gt\progressval
		WriteByte stream, gt\border
		WriteString stream, gt\defaulturl$
		WriteByte stream, gt\defaultitem
		WriteString stream, gt\tips$
		WriteByte stream, gt\itemcount

		; 1.1.0 changes

		WriteByte stream, gt\tabitem
		WriteString stream, gt\group$

		; 1.2 additions

		WriteString stream, gt\autolabel$
		WriteByte stream, gt\largechildx
		WriteByte stream, gt\largechildy
		WriteByte stream, gt\integer
		WriteFloat stream, gt\stp#
		WriteFloat stream, gt\minval#
		WriteFloat stream, gt\maxval#
		WriteString stream, gt\suffix$
		WriteString stream, gt\prefix$

		; add other version changes here. update loadgadget() accordingly

		If gt\childlist <> Null
			WriteByte stream, 1						; indicate a new childlist is coming
			SaveGadgetList( gt\childlist, stream )	; do it
		EndIf
		l = l\nxt
	Wend
	WriteByte stream, 4				; end of this childlist
End Function

Function SaveMenuList( ml.mlist, stream )

	; saves the gadgets in the passed list
	; all gtype fields are saved cos i am lazy

	While ml <> Null
		WriteByte stream, 2							; a menu follows
		mt.mtype = ml\mt
		WriteByte stream, mt\version
		WriteByte stream, mt\active
		WriteByte stream, mt\checkable
		WriteByte stream, mt\checked
		WriteByte stream, mt\id
		WriteString stream, mt\label$
		WriteString stream, mt\shortcut$
		WriteByte stream, mt\shortcutmod

		; changes from 1.0.1
		; add other version changes here. update loadmenuitems() accordingly

		If mt\childlist <> Null
			WriteByte stream, 1						; indicate a new childlist is coming
			SaveMenuList( mt\childlist, stream )	; do it
		EndIf

		ml = ml\nxt
	Wend
	WriteByte stream, 4				; end of this childlist
End Function

Function HideInputGadgets()

	; this function hides all interaction gadgets
	HideGadget PANELCOL
	HideGadget PANELIMG
	HideGadget AREACOL
	HideGadget DEF
	HideGadget AREATXT
	HideGadget PROPTEXT
	HideGadget PROPSELECT
	HideGadget PROPCOMBO
	HideGadget MENUCOMBO
	HideGadget MENUTXT
	HideGadget MENUCHECK
End Function

Function StripPath$( file$ )

	; returns a file$ without the path
	For count = Len( file$ ) To 1 Step -1
		If Mid$( file$, count, 1 ) = "\"
			Return Right$( file$,Len(file$) - count )
		EndIf
	Next
	Return file$
End Function
;
;function RebuildGadgetTree()
;	;
;	; rebuilds the complete gadget tree
;	
;	; get rid of old tree
;	freetreeviewnode( treeviewroot(GADGETTREE)
;	
;	; rebuild from rootgt
;	local newroot% = addtreeviewnode( editor\rootgt\label$, GADGETTREE)
;	AddGadgetToTree(editor\rootgt\childlist)
;		
;End Function
;
;function AddGadgetToTree(parent.gtype)
;
;	if parent\childlist <> null
;	
;		
;	EndIf
;	
;	
;End Function