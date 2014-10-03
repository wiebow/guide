;
; main gadgets function (definitions, create, interact, etc.)
;

;#Region definitions

Type Gtype
	Field childlist.list			; linked list of possible children of this gadget
	Field parent.gtype				; parent gtype of this gadget
	Field garbage					; flag
	Field node						; tree node handle
	Field hide						; flag
	Field drawn						; flag
	Field moved						; flag

	; global gagdet fields
	Field name$						; internal gadget name
	Field version					; gadget type
	Field enable					; true or false
	Field xpos#, ypos#				; position..... floats for snaptogrid
	Field oldx#, oldy#				; storage for position when we move gadget of its parent
	Field width,height				; dimensions
	Field label$					; gadget text
	Field items$					; items, seperated by ;
	Field defaultstring$			; default string
	Field r,g,b						; background color
	Field textr, textg, textb		; text color
	Field group$					; gadget belongs to this group
	Field autolabel$				; auto label for text gadgets
	Field action$					; !NEW action to export for certain gadgets (buttons, etc)

	Field layoutleft				; setgadgetstyle flags
	Field layoutright
	Field layouttop
	Field layoutbottom

	; window
	Field titlebar
	Field resize
	Field menu
	Field statusbar
	Field tool
	Field client

	; graphic, panel
	Field image$					; image file name
	Field imagehandle				; image is loaded here

	; canvas
	Field pointershow				; flag

	; checkbox, radiobutton
	Field checked

	; textfield
	Field mask

	;textarea
	Field wordwrap

	; slider, splitter
	Field direction					; flag. if false 0 then slider is horizontal

	; progressbar
	Field progressval#				; default value for progressbar

	;panel,tabpanel,label
	Field border					; flag
	Field tabitem					; item this tabpanel belongs to
	Field largechildx				; !NEW flag. true if panel contains a canvas which is wider than the panel
	Field largechildy				; !NEW flag. true if panel contains a canvas which is larger than the panel
;	Field offsetx, offsety			; !NEW offset of child parent if it is larger

	;html
	Field defaulturl$

	;combobox, tabber
	Field defaultitem				; default selected item

	;toolbar
	Field tips$

	;tabber
	Field itemcount

	;spinner
	Field integer					; !NEW flag. if false then spinner contains a float
	Field stp#						; !NEW amount to change when spinned
	Field minval#, maxval#			; !NEW value restraints
	Field suffix$					; !NEW auto suffix in spinners
	Field prefix$					; !NEW auto prefix in spinners

End Type
	
;#End Region

;#Region Functions

Function CreateGadget.gtype( version, clone=False )

	; creates a gadget type containing defaults

	Local parent.gtype, gt.gtype

	gt = New gtype
	gt\version = version
	gt\enable = True

	If version <> WINDOW Then SetParent( gt, editor\selection, clone )

	Select version
		Case SPLITTER
			gt\name$ = "splSplit" + editor\splittercount
			gt\width = 8
			gt\height = 32
			gt\direction = 1			; vertical
			gt\group$ = "split" + editor\splittercount

			gt\layoutleft = 0
			gt\layoutright = 1
			gt\layouttop = 1
			gt\layoutbottom = 1

		Case COLSELECTOR
			gt\name$ = "colSel" + editor\colorcount

			gt\width = 32
			gt\height = 32

			gt\r = 255
			gt\g = 255
			gt\b = 0

			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case IMAGE
			gt\name$ = "imgBox" + editor\imagecount

			gt\width = 64
			gt\height = 64

			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case SPINNER
			gt\name$ = "sprSpinner" + editor\spinnercount
			gt\integer = True
			gt\stp = 1
			gt\defaultstring = "1"
			gt\minval = 0
			gt\maxval = 1
			gt\width = 32
			gt\height = 20
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case FOLDBOX1
			If gt\parent <> Null			; foldbox has parent? SetParent makes sure it can only be a panel
				gt\name$ = "fbxFoldBox" + editor\foldboxcount
				gt\label$ = gt\name$
				gt\xpos = gt\parent\xpos
				gt\ypos = gt\parent\ypos
				gt\width = gt\parent\width
				gt\height = 64

				; place this foldbox underneath other foldboxes on this panel. calculate ypos

				Local l.list = gt\parent\childlist

				While l <> Null
					If l\gt <> gt And l\gt\version = FOLDBOX1
						gt\ypos = gt\ypos + l\gt\height
					EndIf
					l = l\nxt
				Wend

			Else
				; cannot create foldbox on anything other than panels. delete new gadget
				Delete gt : Return
			EndIf

		Case SEPLINE
			gt\name$ = "sepLine" + editor\linecount
			gt\width = 64
			gt\height = 16
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case GROUPBOX
			gt\name$ = "grpGroupBox" + editor\groupboxcount
			gt\label$ = gt\name$
			gt\width = 64
			gt\height = 64
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case WINDOW
			gt\name$ = "EditWindow"
			gt\label$ = gt\name$
			gt\parent = Null

			gt\width = 400
			gt\height = 400

			gt\xpos = GadgetX( MAINWINDOW ) + GadgetWidth( MAINWINDOW )
			gt\ypos = GadgetY( MAINWINDOW )

			gt\menu = False
			gt\resize = True
			gt\statusbar = False
			gt\titlebar = True
			gt\tool = False
			gt\client = False

			; set tree root
			gt\node = AddTreeViewNode( gt\name$, TreeViewRoot( GADGETTREE ), WINDOW )

			; create the window as child window of mainwindow
			EDITWINDOW = CreateWindow( gt\name$, gt\xpos,gt\ypos, gt\width,gt\height,MAINWINDOW,1+2+16 )

			; draw gadgets here
			DISPLAYCANVAS = CreateCanvas( 0,0, ClientWidth( EDITWINDOW ), ClientHeight( EDITWINDOW ), EDITWINDOW )

			; create menu canvas
			MENUCANVAS = CreateCanvas( 0,0, ClientWidth( EDITWINDOW ), 20, EDITWINDOW )
			HideGadget MENUCANVAS

			; create root menu item
			editor\rootmt = New mtype

			; add menu node to editwin node
			editor\rootmt\node = AddTreeViewNode( "MENU", gt\node, MENUROOT )
			editor\rootmt\version = MENUROOT
			editor\rootmt\label$ = "MENU"
			editor\rootmt\id = 0

			; show it
			ExpandTreeViewNode gt\node
			Return gt

		Case BUTTON
			gt\name$ = "btnButton" + editor\buttoncount
			gt\label$ = gt\name$
			gt\width = 64
			gt\height = 24
			gt\layoutleft = 1												; setgadgetlayout flags
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case LABEL
			gt\name$ = "lblLabel" + editor\labelcount
			gt\label$ = gt\name$
			gt\width = 64
			gt\height = 16
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case TEXTFIELD
			gt\name$ = "tfdTextField" + editor\textfieldcount
			gt\defaultstring$ = "textfield"
			gt\width = 96
			gt\height = 20
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0
			gt\mask = False

		Case TEXTAREA
			gt\name$ = "teaTextArea" + editor\textareacount
			gt\defaultstring$ = gt\name$
			gt\width = 96
			gt\height = 64
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0
			gt\wordwrap	= False				; style parameter
			gt\r = editor\winr				; backdrop col
			gt\g = editor\wing
			gt\b = editor\winb
			gt\textr = editor\win_text_r		; text col
			gt\textg = editor\win_text_g
			gt\textb = editor\win_text_b

		Case CHECKBOX
			gt\name$ = "chbCheckBox" + editor\checkboxcount
			gt\label$ = gt\name$
			gt\checked = False
			gt\width = 96
			gt\height = 16
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case RADIOBUTTON
			gt\name$ = "rabRadioButton" + editor\radiobuttoncount
			gt\label$ = gt\name$
			gt\checked = False
			gt\width = 96
			gt\height = 16
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case LISTBOX
			gt\name$ = "libListBox" + editor\listboxcount
			gt\label$ = gt\name$
			gt\width = 96
			gt\height = 96
			gt\layoutleft = 1
			gt\layoutright = 2
			gt\layouttop = 1
			gt\layoutbottom = 2
			gt\items$ = "item0"

		Case COMBOBOX
			gt\name$ = "cobComboBox" + editor\comboboxcount
			gt\items$ = gt\name$
			gt\width = 96
			gt\height = 20
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0
			gt\defaultitem = 0

		Case TREEVIEW
			gt\name$ = "trvTreeView" + editor\treeviewcount
			gt\items$ = gt\name$
			gt\width = 96
			gt\height = 96
			gt\layoutleft = 1
			gt\layoutright = 0
			gt\layouttop = 1
			gt\layoutbottom = 0

		Case TABBER
			gt\name$ = "tabTabber" + editor\tabbercount
			gt\width = 96
			gt\height = 96
			gt\layoutleft = 1
			gt\layoutright = 2
			gt\layouttop = 1
			gt\layoutbottom = 2
			gt\items$ = "Item0"
			gt\itemcount = 1

		Case PROGRESSBAR
			gt\name$ = "prbProgressBar" + editor\progresscount
			gt\width = 96
			gt\height = 16
			gt\layoutleft = 1
			gt\layoutright = 2
			gt\layouttop = 1
			gt\layoutbottom = 2
			gt\progressval = 0.0

		Case HTML
			gt\name$ = "htmHTMLview" + editor\htmlcount
			gt\width = 96
			gt\height = 96
			gt\layoutleft = 1
			gt\layoutright = 1
			gt\layouttop = 1
			gt\layoutbottom = 1

		Case CANVAS
			gt\name$ = "cavCanvas" + editor\canvascount
			gt\width = 96
			gt\height = 96
			gt\layoutleft = 1
			gt\layoutright = 2
			gt\layouttop = 1
			gt\layoutbottom = 2
			gt\pointershow = True

		Case PANEL
			gt\name$ = "panPanel" + editor\panelcount
			gt\width = 96
			gt\height = 96
			gt\layoutleft = 1
			gt\layoutright = 1
			gt\layouttop = 1
			gt\layoutbottom = 1
			gt\border = False
			gt\r = editor\gadr
			gt\g = editor\gadg
			gt\b = editor\gadb
			gt\image$ = ""

		Case TABPANEL
			gt\name$ = "TabPanel" + editor\tabpanelcount
			gt\xpos = gt\parent\xpos
			gt\ypos = gt\parent\ypos + 20
			gt\width = gt\parent\width
			gt\height = gt\parent\height -20
			gt\layoutleft = 1
			gt\layoutright = 1
			gt\layouttop = 1
			gt\layoutbottom = 1

		Case SLIDER
			gt\name$ = "slrSlider" + editor\slidercount
			gt\width = 64
			gt\height = 16
			gt\layoutleft = 1
			gt\layoutright = 1
			gt\layouttop = 0
			gt\layoutbottom = 1
			gt\direction = 0			; horizontal
	End Select

	parent = gt\parent
	Editor\changed = True
	SnapGadgetToGrid( gt )
	UpdateCounter( gt\version )

	; add gadget to the treeview
	gt\node = AddTreeViewNode( gt\name$, parent\node, gt\version )
	ExpandTreeViewNode parent\node

	If gt\version <> FOLDBOX1 And gt\version <> TABPANEL	; exceptions

		; check size of new gadget. if larger than parent, resize it
		If gt\width >= parent\width Then gt\width = parent\width - editor\grid\size * 2
		If gt\height >= parent\height Then gt\height = parent\height - editor\grid\size * 2

		; position in center of parent if needed
		gt\xpos = parent\xpos + parent\width / 2 - gt\width / 2
		gt\ypos = parent\ypos + parent\height / 2 - gt\height / 2
	EndIf

	; put gadget on grid
	SnapGadgetToGrid( gt )

	If gt\version <> TABPANEL
		; show information about new gadget
		ShowGadgetDetails( gt )

		; get rid of current selections, and select new gadget
		LL_ClearList( editor\selection )
		LL_AddItem( gt, editor\selection )
		SelectTreeViewNode gt\node
	EndIf

	; if we created a tabber, create tabpanel on it
	If gt\version = TABBER Then ManageTabber(gt)
	Return gt
End Function

Function LoadGadget( stream, parent.gtype, version$ )

	; loads gadgets and adds it to parents' childlist
	; differences in release versions are caught too

	Local gt.gtype

	Local nxt = ReadByte( stream )

	While nxt = 2		; then a gadget follows

		gt = New gtype

		gt\parent = parent
		gt\version = ReadByte( stream )
		gt\hide = ReadByte( stream )
		gt\name$ = ReadString( stream )
		gt\enable = ReadByte( stream )
		gt\xpos = ReadInt( stream )
		gt\ypos = ReadInt( stream )
		gt\width = ReadInt( stream )
		gt\height = ReadInt( stream )
		gt\label$ = ReadString( stream )
		gt\items$ = ReadString( stream )
		gt\defaultstring$ = ReadString( stream )
		gt\r = ReadByte( stream )
		gt\g = ReadByte( stream )
		gt\b = ReadByte( stream )
		gt\textr = ReadByte( stream )
		gt\textg = ReadByte( stream )
		gt\textb = ReadByte( stream )
		gt\layoutleft = ReadByte( stream )
		gt\layoutright = ReadByte( stream )
		gt\layouttop = ReadByte( stream )
		gt\layoutbottom = ReadByte( stream )

		gt\image$ = ReadString( stream )
		gt\pointershow = ReadByte( stream )
		gt\checked = ReadByte( stream )
		gt\mask = ReadByte( stream )
		gt\wordwrap = ReadByte( stream )
		gt\direction = ReadByte( stream )
		gt\progressval = ReadFloat( stream )
		gt\border = ReadByte( stream )
		gt\defaulturl$ = ReadString( stream )
		gt\defaultitem = ReadByte( stream )
		gt\tips$ =ReadString( stream )
		gt\itemcount = ReadByte( stream )

		; !!! version changes go here !!!

		Select version$
			Case "1.1.0"
				gt\tabitem = ReadByte( stream )
				gt\group$ = ReadString( stream )

			Case "1.2"

				; 1.1.0 additions

				gt\tabitem = ReadByte( stream )
				gt\group$ = ReadString( stream )

				; 1.2 additions

				gt\autolabel$ = ReadString( stream )
				gt\largechildx = ReadByte( stream )
				gt\largechildy = ReadByte( stream )
				gt\integer = ReadByte( stream )
				gt\stp# = ReadFloat( stream )
				gt\minval# = ReadFloat( stream )
				gt\maxval# = ReadFloat( stream )
				gt\suffix$ = ReadString( stream )
				gt\prefix$ = ReadString( stream )

			Case "1.3", "1.4"

				; 1.1.0 additions

				gt\tabitem = ReadByte( stream )
				gt\group$ = ReadString( stream )

				; 1.2 additions

				gt\autolabel$ = ReadString( stream )
				gt\largechildx = ReadByte( stream )
				gt\largechildy = ReadByte( stream )
				gt\integer = ReadByte( stream )
				gt\stp# = ReadFloat( stream )
				gt\minval# = ReadFloat( stream )
				gt\maxval# = ReadFloat( stream )
				gt\suffix$ = ReadString( stream )
				gt\prefix$ = ReadString( stream )

				; 1.3 additions

				If gt\version = IMAGE And gt\image$ <> ""
					gt\imagehandle = LoadImage( gt\image$ )
				EndIf

				; add new versions here
				; copy previous changes and add new ones

		End Select

		; catch contante changes
		If gt\version = 96 Then gt\version = TABPANEL

		; add gadget to the treeview
		gt\node = AddTreeViewNode( gt\name$, parent\node, gt\version )

		; update counter
		UpdateCounter( gt\version )

		; add gadget to parent childlist
		LL_AddItem( gt, parent\childlist, LL_CHILD )

		nxt = ReadByte( stream )
		If nxt = 1
			LoadGadget( stream, gt, version$ ) 	; new childlist follows. this gadget is the parent
			nxt = ReadByte( stream )			; after returning here, check if there is a next gadget
		EndIf

	Wend

End Function

Function UnhideGadgets()

	; unhides ALL gadgets

	For gt.gtype = Each gtype
		gt\hide = False
	Next
	If editor\selection <> Null Then ShowGadgetDetails( editor\selection\gt )

End Function
	
;;; <summary>Copies settings between passed gadgets</summary>
;;; <param name="s">source gadget</param>
;;; <param name="d">desination gadget</param>
;;; <remarks></remarks>
;;; <returns>nothing</returns>
;;; <subsystem>GUIDE.Gadgets</subsystem>
;;; <example>CopyGadget( MyGadget, NewGadget )</example>
Function CopyGadgetSettings( s.gtype, d.gtype )

	If s = Null Or d = Null Then RuntimeError "CopyGadgetSettings: Gadget does not exist!"
	d\width = s\width
	d\height = s\height
	d\label$ = s\label$
	d\items$ = s\items$
	d\defaultstring$ = s\defaultstring$
	d\r = s\r
	d\g = s\g
	d\b = s\b
	d\textr = s\textr
	d\textg = s\textg
	d\textb = s\textb
	d\layoutleft = s\layoutleft
	d\layoutright = s\layoutright
	d\layouttop = s\layouttop
	d\layoutbottom = s\layoutbottom
	d\image$ = s\image$
	d\imagehandle = s\imagehandle
	d\pointershow = s\pointershow
	d\checked = s\checked
	d\mask = s\mask
	d\wordwrap = s\wordwrap
	d\direction = s\direction
	d\progressval# = s\progressval#
	d\border = s\border
	d\defaulturl$ = s\defaulturl$
	d\defaultitem = s\defaultitem
	d\tips$ = s\tips$
	d\itemcount = s\itemcount
	d\group$ = s\group$
	d\tabitem = s\tabitem
	d\drawn = s\drawn
	d\moved = s\moved
	d\hide = s\hide
	d\garbage = s\garbage
	d\autolabel$ = s\autolabel$
	d\largechildx = s\largechildx
	d\largechildy = s\largechildy
	d\integer = s\integer
	d\stp# = s\stp#
	d\minval# = s\minval#
	d\maxval# = s\maxval#
	d\suffix$ = s\suffix$
	d\prefix$ = s\prefix$

End Function

;;; <summary>Selects all the text in passed TextField gadget</summary>
;;; <param name="textgad">TextField gadget</param>
;;; <remarks></remarks>
;;; <returns>nothing</returns>
;;; <subsystem>GUIDE.Gadgets</subsystem>
;;; <example></example>
Function SelectAllText( textgad )
	; selects all text in a textgadget
	api_SendMessage(QueryObject(textgad, 1), $B1, 0, Len(TextFieldText(textgad)))
End Function

Function CloneGadget()

	; clones the 1st gadget in the selection list

	If editor\selection <> Null

		Local oldgt.gtype = editor\selection\gt

		; make an identical gadget
		CreateGadget( oldgt\version, True )

		; new gadget is now in editor\selection

		; make easy to read
		newgt.gtype = editor\selection\gt

		; offset
		newgt\xpos = oldgt\xpos + 16
		newgt\ypos = oldgt\ypos + 16

		; copy settings from old to new gadget

		CopyGadgetSettings( oldgt, newgt )
		UpdateCounter( newgt\version )
	EndIf

End Function

Function ManageTabber(gt.gtype)

	; see if there is a tabpanel for each tab item. if not, create a tabpanel for it
	; we start at item 0

	Local found, itemcount
	Local l.list
	Local newtabpanel.gtype
	
	;
	;get item count based on number of ; in item$ field of tabber gadget
	gt\itemcount = CountItems(gt\items$)
	

	For count = 0 To gt\itemcount -1			; start at item 0
		;
		; try to find a tabpanel belonging to current count
		l = gt\childlist
		found = False
		While l <> Null
			;
			; walk through childlist of tabber
			If l\gt\version = TABPANEL
				If l\gt\tabitem = count
					found = True : Exit
				EndIf
			EndIf
			l = l\nxt
		Wend

		If found = False ; then tabpanel for this tabitem not found. create it
			newtabpanel = CreateGadget(TABPANEL)
			newtabpanel\tabitem = count							; store item this panel belongs to
			;
			; select tabber again
			LL_ClearList(editor\selection)
			LL_AddItem(gt, editor\selection)
		EndIf
	Next

	;
	; try to find out of range tabpanels on this tabber as well
	; delete them
	; tabpanels with item value -1 will not get exported to code.

	l = gt\childlist
	While l <> Null
		If l\gt\tabitem > gt\itemcount Then l\gt\tabitem = -1
		l = l\nxt
	Wend

	SelectTreeViewNode gt\node
	ShowGadgetDetails( gt )

End Function

Function CountItems(items$)

	; counts the # of items in passed string and returns that value
	; items are seperated with ;

	Local itemcount = 0
	;
	; prepare string
	If Right$(items$, 1) <> ";" Then items$ = items$ + ";"
	;
	; count the number of ;
	For count = 1 To Len(items$)
		If Mid$(items$, count, 1) = ";" Then itemcount = itemcount + 1
	Next
	Return itemcount
End Function

Function ScaleFoldBoxes( l.list )

	; loop through passed list, scale any groupboxes width to their parent

	While l.list <> Null
		If l\gt\version = FOLDBOX1
			l\gt\width = l\gt\parent\width
			l\gt\xpos = l\gt\parent\xpos
		EndIf
		l = l\nxt
	Wend

End Function

Function MoveFoldBoxGadget( gt.gtype, dir )

	Local l.list
	Local this.gtype

	; moves foldbox in childlist of its parent, eg: swap position with prev or nxt foldbox
	; if dir = 0 then move up, else move down

	; find the passed foldbox in its parent childlist

	l = gt\parent\childlist

	While l <> Null

		If l\gt = gt

			; found it!

			If dir = 0					 ; then try to swap position with previous box; move 'up'

				If l\prev <> Null
					SwapFoldBoxPositions( l\prev\gt, l\gt )

					this = gt
					l\gt = l\prev\gt
					l\prev\gt = this
					Return
				EndIf
			Else						; try to move 'down'
				If l\nxt <> Null

					SwapFoldBoxPositions( l\gt, l\nxt\gt )

					this = gt
					l\gt = l\nxt\gt
					l\nxt\gt = this
					Return

				EndIf
			EndIf
		EndIf

		l = l\nxt

	Wend

End Function

Function SwapFoldBoxPositions( b1.gtype, b2.gtype )

	; swaps foldbox yposition of passes boxes
	; and moves it children accordingly

	; determine new positions for both boxes

	b2newy = b1\ypos
	b1newy = b2newy + b2\height

	; get movement distance for each box

	b1move = b1newy - b1\ypos
	b2move = b2newy - b2\ypos

	; swap boxes position

	b1\ypos = b1newy
	b2\ypos = b2newy

	; move the children of both boxes using distance traveled of their parents

	MoveGadgets( 0, b1move, b1\childlist, True )
	MoveGadgets( 0, b2move, b2\childlist, True )

End Function

Function MoveFoldBoxPosition( l.list, distance )

	; moves foldboxes on the panel. used by deletelist() when deleting a foldbox

	; loop through list, and move the foldboxes up

	While l <> Null

		l\gt\ypos = l\gt\ypos - distance

		; and the children

		MoveGadgets( 0, -distance, l\gt\childlist, True )

		l = l\nxt
	Wend

End Function

Function ActiveTabPanel.gtype( tabber.gtype )

	; returns the currently active tabpanel

	Local l.list = tabber\childlist

	While l <> Null
		If l\gt\tabitem = tabber\defaultitem Then Return l\gt
		l = l\nxt
	Wend

	Return Null

End Function

Function GadgetChildHit.gtype( xpos, ypos, l.list )

	; returns gadget pointer if passed position is inside any gadget of the passed childlist
	; also checks children of a hit gadget

	Local result.gtype = Null

	While l <> Null

		If GadgetHit( xpos,ypos, l\gt )

			; hit on l\gt !
			; check hit on children

			If l\gt\version = TABBER

				; gadgets on tabbers are on the tabpanels. find the displayed
				; tab panel and check its children

				li.list = l\gt\childlist

				While li <> Null
					If li\gt\version = TABPANEL And li\gt\tabitem = l\gt\defaultitem

						result = GadgetChildHit( xpos,ypos, li\gt\childlist )
						Exit
					EndIf

					li = li\nxt
				Wend
			Else
				result = GadgetChildHit( xpos,ypos, l\gt\childlist )
			EndIf

			If result <> Null Then Return result

			; no hit on child gadget

			; never return a TABPANEL as hit

			If l\gt\version = TABPANEL Then Return Null

			Return l\gt

		EndIf

		l = l\nxt
	Wend

	Return Null

End Function

Function GadgetHit( xpos, ypos, gt.gtype )

	; returns true if the passed coordinates are inside the passed gadget

	; do not return true on editwindow and tabpanels
	; do not check hidden or undrawn gadgets

	Local width% = gt\width
	Local height% = gt\height
	Local y% = gt\ypos

	; never select the following gadgets:

	If gt\version = WINDOW Then Return False
	If gt\hide = True Then Return False
	If gt\drawn = False Then Return False

	; only check hits on 'titlebar' of the following gadgets

	If gt\version = GROUPBOX Or gt\version = TABBER Or gt\version = FOLDBOX1 Or gt\version = PANEL Or gt\version = CANVAS
		height = 20
		If gt\version = PANEL Then y = y - 8
	EndIf

	; do hit check

	If PointInRect( xpos, ypos, gt\xpos, y, width, height ) Then Return True

	Return False

End Function

Function GadgetInBox( gt.gtype )

	; returns true if any side of passed gadget is inside selection box
	; dont select tabberpanels, hidden or not drawn gadgets

	If gt\version = TABPANEL Then Return False
	If gt\hide = True Then Return False
	If gt\drawn = False Then Return False

	; ok, do check

	Local xpos = editor\bx
	Local ypos = editor\by
	Local width = editor\grid\cx - editor\bx
	Local height = editor\grid\cy - editor\by

	; wrap the box if needed

	If width < 0
		xpos = editor\bx + width
		width = Abs(width)
	EndIf

	If height < 0
		ypos = editor\by + height
		height = Abs(height)
	EndIf

	; top left
	If PointInRect( gt\xpos, gt\ypos, xpos,ypos,width,height ) Then Return True
	; top right
	If PointInRect( gt\xpos+gt\width, gt\ypos, xpos,ypos,width,height ) Then Return True
	; bottom left
	If PointInRect( gt\xpos, gt\ypos+gt\height, xpos,ypos,width,height ) Then Return True
	; bottom right
	If PointInRect( gt\xpos+gt\width, gt\ypos+gt\height, xpos,ypos,width,height ) Then Return True

	Return False

End Function

Function SelectTabberItem( gt.gtype )

	; checks if we hit the item of a tabber and selects it

	; position of mouse on canvas
	Local mxpos = MouseX( DISPLAYCANVAS )
	Local mypos = MouseY( DISPLAYCANVAS )

	; origin of selection rect

	Local rectx = gt\xpos

	Local laststart = 1
	Local lastsep = 0
	Local tabcount = 0

	; make sure itemlist has proper ending

	items$ = gt\items$
	If Right$( items$, 1 ) <> ";" Then items$ = items$ + ";"

	; check each item

	For count = 1 To Len( items$ )

		If Mid$( items$, count, 1 ) = ";"
			lastsep = count
			txt$ = Mid$( items$, laststart, lastsep-laststart )
			laststart = count + 1

			; determine width of item
			width = StringWidth( txt$ ) + 20

			If PointInRect( mxpos,mypos, rectx,gt\ypos, width,20 )

				; clicked here!
				gt\defaultitem = tabcount

				; show correct panel
				ShowTabberPanel( gt )

				; only select tabber gadget
				LL_ClearList( editor\selection )
				LL_AddItem( gt, editor\selection )

				Return

			EndIf

			tabcount = tabcount + 1
			rectx = rectx + width

		EndIf
	Next

End Function

Function ShowTabberPanel( gt.gtype )

	; shows correct tabberpanel for passed tabber gadget

	l.list = gt\childlist

	While l <> Null

		If l\gt\version = TABPANEL
			If l\gt\tabitem = gt\defaultitem
				l\gt\hide = False
				UnhideSelection( l\gt\childlist )
			Else
				l\gt\hide = True
				HideSelection( l\gt\childlist )
			EndIf
		EndIf

		l = l\nxt
	Wend

End Function

Function SetParent( gt.gtype, selection.list, clone=False )

	; sets a valid parent for passed gadget

	; the default parent is the gadget in the selection. if this parent is not valid for this type
	; of gadget, we will 'walk' up and use the first valid parent.
	; if clone is true then the parent will be the parent of the selection.
	; if no selection is passed, then the main window is used as parent.

	Local parent.gtype, l.list

	; determine parent

	If selection = Null

		; catch illegal foldbox parent

		If gt\version = FOLDBOX1 Then Return

		parent = editor\rootgt
	Else
		; foldbox cannot be added on anything else than a panel

		If gt\version = FOLDBOX1 And selection\gt\version <> PANEL Then Return

		If clone = True
			parent = selection\gt\parent
		Else
			parent = selection\gt
		EndIf
	EndIf

	; we cannot create gadgets on tabbers unless it is a TABPANEL
	; if this is not the case, we have to use the TABPANEL associated with the
	; currently selected tabitem and use it as parent

	If parent\version = TABBER And gt\version <> TABPANEL

		; find the active tabpanel in the childlist of the tabber

		parent = ActiveTabPanel( parent )

	EndIf

	; no foldboxes on foldboxes

	If parent\version = FOLDBOX1 And gt\version = FOLDBOX1 Then parent = parent\parent

	; check parent version. if it is valid, then set it. if not, use the parents' parent, which
	; CANNOT be invalid

	If parent\version = WINDOW Or parent\version = CANVAS Or parent\version = PANEL Or parent\version = GROUPBOX Or parent\version = TABPANEL Or parent\version = TABBER Or parent\version = FOLDBOX1
		gt\parent = parent
	Else
		gt\parent = parent\parent
	EndIf

	; add gadget to childlist of parent

	LL_AddItem( gt, gt\parent\childlist, LL_CHILD )

End Function

Function MoveGadgets( xmoved, ymoved, l.list, force = False, key = False )

	; moves gadgets in passed list and re-sets the moved flag

	; if force is true then skip constraint. this is used when moving gadgets by entering a new
	; position instead of dragging it

	; if key is true then this function has been called by the use of keys

	; check all gadgets in selection to see if any of them cannot move. if that is the
	; case, then skip movement!

	If l = Null Then Return		; get outta here if the list is empty

	hit = False

	If force = False Then hit = CheckConstraint( xmoved, ymoved, l.list )

	; move selection if no constraint was hit or when constraint is off

	If hit = False Or editor\constraint = False

		MoveGadgetInternal( xmoved, ymoved, l.list, key )

		Local gt.gtype
		For gt = Each gtype
			gt\moved = False
		Next

		editor\changed = True
	EndIf

End Function

Function MoveGadgetInternal( xmoved, ymoved, l.list, key=False )

	; moves selected gadget and its children. helper function, called by movegadgets()

	; if key is false then gadgets have been dragged. if key is true and we try to move a foldbox
	; then exit

	Local parent.gtype

 	While l <> Null

		If l\gt\moved = False

;			If key = False;True
;				If l\gt\version = FOLDBOX1 Then Exit
;			EndIf

	 		l\gt\xpos = l\gt\xpos + xmoved
		 	l\gt\ypos = l\gt\ypos + ymoved
			l\gt\moved = True				; set move flag so this gadget won't get moved again

			; call again for possible children

			MoveGadgetInternal( xmoved, ymoved, l\gt\childlist, key )

		EndIf

		l = l\nxt
	Wend

End Function

Function UpdateCounter( gadgetid )

	; updates the counter of the passed gadget

	Select gadgetid
		Case BUTTON
			editor\buttoncount = editor\buttoncount + 1
		Case LABEL
			editor\labelcount = editor\labelcount + 1
		Case TEXTFIELD
			editor\textfieldcount = editor\textfieldcount + 1
		Case TEXTAREA
			editor\textareacount = editor\textareacount + 1
		Case CHECKBOX
			editor\checkboxcount = editor\checkboxcount + 1
		Case RADIOBUTTON
			editor\radiobuttoncount = editor\radiobuttoncount +1
		Case LISTBOX
			editor\listboxcount = editor\listboxcount + 1
		Case COMBOBOX
			editor\comboboxcount = editor\comboboxcount +1
		Case TREEVIEW
			editor\treeviewcount = editor\treeviewcount + 1
		Case HTML
			editor\htmlcount = editor\htmlcount + 1
		Case TABBER
			 editor\tabbercount = editor\tabbercount + 1
		Case PROGRESSBAR
			editor\progresscount = editor\progresscount + 1
		Case CANVAS
			editor\canvascount = editor\canvascount + 1
		Case PANEL
			editor\panelcount = editor\panelcount + 1
		Case SLIDER
			editor\slidercount = editor\slidercount + 1
		Case GROUPBOX
			editor\groupboxcount = editor\groupboxcount + 1
		Case SEPLINE
			editor\linecount = editor\linecount + 1
		Case MENUITEM
			editor\menuidcount = editor\menuidcount + 1
		Case SPINNER
			editor\spinnercount = editor\spinnercount + 1
		Case TABPANEL
			editor\tabpanelcount = editor\tabpanelcount + 1
		Case FOLDBOX1
			editor\foldboxcount = editor\foldboxcount + 1
		Case IMAGE
			editor\imagecount = editor\imagecount + 1
		Case COLSELECTOR
			editor\colorcount = editor\colorcount + 1
		Case SPLITTER
			editor\splittercount = editor\splittercount + 1
		Case SCENE3D
			editor\scene3dcount = editor\scene3dcount + 1
	End Select
End Function

Function ResetGadgetCounters()

	editor\buttoncount = 0
	editor\labelcount = 0
	editor\textfieldcount = 0
	editor\textareacount = 0
	editor\checkboxcount = 0
	editor\listboxcount = 0
	editor\comboboxcount = 0
	editor\tabbercount = 0
	editor\canvascount = 0
	editor\panelcount = 0
	editor\radiobuttoncount = 0
	editor\slidercount = 0
	editor\progresscount = 0
	editor\htmlcount = 0
	editor\treeviewcount = 0
	editor\groupboxcount = 0
	editor\linecount = 0
	editor\menuidcount = 1
	editor\tabpanelcount = 0
	editor\spinnercount = 0
	editor\foldboxcount = 0
	editor\imagecount = 0
	editor\colorcount = 0
	editor\splittercount = 0
	editor\scene3dcount = 0

End Function

Function ShowGadgetDetails( gt.gtype, delinput = True )

	; shows the necessary items in the GADGETPROPBOX

	If delinput = True Then HideInputGadgets()

	SetGadgetText VERSIONLABEL, "Type:"
	ClearGadgetItems GADGETPROPBOX
	SetStatusText MAINWINDOW, ""

	Local FREE$, LOCKED$, FLOATS$


	; show gadgetproperties according to selected language

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

	If gt\hide = True Then append$ = " [hidden]"

	Select gt\version
		Case WINDOW
			SetGadgetText VERSIONLABEL, "Type: Window" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;2
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;3
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;4
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;5

			AddGadgetItem GADGETPROPBOX, "Titlebar: " + gt\titlebar		;6
			AddGadgetItem GADGETPROPBOX, "Resizeable: " + gt\resize		;7
			AddGadgetItem GADGETPROPBOX, "Menu: " + gt\menu				;8
			AddGadgetItem GADGETPROPBOX, "Statusbar: " + gt\statusbar	;9
			AddGadgetItem GADGETPROPBOX, "Tool: " + gt\tool				;10
			AddGadgetItem GADGETPROPBOX, "Client coords: " + gt\client	;11

		Case SPLITTER
			SetGadgetText VERSIONLABEL, "Type: Splitter" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4

			If gt\direction = 0
				txt$ = "Horizontal"
			Else
				txt$ = "Vertical"
			EndIf
			AddGadgetItem GADGETPROPBOX, "Direction: " + txt$		;5
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;6
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;7

		Case COLSELECTOR
			SetGadgetText VERSIONLABEL, "Type: ColorSelector" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Default Color: " + gt\r + "," + gt\g + "," + gt\b	;5
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable			;6
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$		;7

		Case IMAGE
			SetGadgetText VERSIONLABEL, "Type: ImageBox" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Image: " + gt\image$		;5

		Case SPINNER
			SetGadgetText VERSIONLABEL, "Type: Spinner" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4

			If gt\integer = True
				txt$ = "Int"
			Else
				txt$ = "Float"
			EndIf
			AddGadgetItem GADGETPROPBOX, "Type: " + txt$			;5

			If gt\integer = True
				txt$ = Int( gt\stp )
			Else
				txt$ = Float( gt\stp )
			EndIf

			AddGadgetItem GADGETPROPBOX, "Default value: " + gt\defaultstring$		;6
			AddGadgetItem GADGETPROPBOX, "Minimum value: " + gt\minval	;7
			AddGadgetItem GADGETPROPBOX, "Maximum value: " + gt\maxval	;8
			AddGadgetItem GADGETPROPBOX, "Change amount: " + txt$		;9
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable			;10
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$		;11
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\autolabel$	;12
			AddGadgetItem GADGETPROPBOX, "Prefix: " + gt\prefix$	;13
			AddGadgetItem GADGETPROPBOX, "Suffix: " + gt\suffix$	;14

		Case FOLDBOX1
			SetGadgetText VERSIONLABEL, "Type: Foldbox" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;2
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;3

		Case GROUPBOX
			SetGadgetText VERSIONLABEL, "Type: Groupbox" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;2
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;3
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;4
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;5
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;6
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;7

		Case SEPLINE
			SetGadgetText VERSIONLABEL, "Type: Seperator Line" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;2
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;3
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;4
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;5
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;6
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;7

			If gt\direction = 0
				txt$ = "Horizontal"
			Else
				txt$ = "Vertical"
			EndIf
			AddGadgetItem GADGETPROPBOX, "Direction: " + txt$		;8

		Case BUTTON
			SetGadgetText VERSIONLABEL, "Type: Button" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;2
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;3
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;4
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;5
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;6
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;7

		Case LABEL
			SetGadgetText VERSIONLABEL, "Type: Label" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;2
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;3
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;4
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;5

			Select gt\border
				Case 0,2
					txt$ = "None"
				Case 1
					txt$ = "Flat"
				Case 3
					txt$ = "Sunken 3D"
			End Select
			AddGadgetItem GADGETPROPBOX, "Border: " + txt$			;6

			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;8

		Case TEXTFIELD
			SetGadgetText VERSIONLABEL, "Type: Textfield" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Default string: " + gt\defaultstring$		;5
			AddGadgetItem GADGETPROPBOX, "Masked text: " + gt\mask					;6
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable						;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$					;8
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\autolabel$	;9

		Case TEXTAREA
			SetGadgetText VERSIONLABEL, "Type: Textarea" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Default string: " + gt\defaultstring$					;5
			AddGadgetItem GADGETPROPBOX, "Font color: " + gt\textr+","+gt\textg+","+gt\textb	;6
			AddGadgetItem GADGETPROPBOX, "Background color: " + gt\r+","+gt\g+","+gt\b			;7
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable									;8
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;9

		Case CHECKBOX
			SetGadgetText VERSIONLABEL, "Type: Checkbox" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;2
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;3
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;4
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;5
			AddGadgetItem GADGETPROPBOX, "Checked: " + gt\checked	;6
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;8

		Case RADIOBUTTON
			SetGadgetText VERSIONLABEL, "Type: Radiobutton" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\label$		;1
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;2
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;3
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;4
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;5
			AddGadgetItem GADGETPROPBOX, "Checked: " + gt\checked	;6
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;8

		Case LISTBOX
			SetGadgetText VERSIONLABEL, "Type: Listbox" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Items: " + gt\items$		;5
			AddGadgetItem GADGETPROPBOX, "Default item: " + gt\defaultitem	;6
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;8

		Case COMBOBOX
			SetGadgetText VERSIONLABEL, "Type: Combobox" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Items: " + gt\items$		;5
			AddGadgetItem GADGETPROPBOX, "Default item: " + gt\defaultitem	;6
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;8
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\autolabel$	;9

		Case TREEVIEW
			SetGadgetText VERSIONLABEL, "Type: Treeview" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;5
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;6

		Case TABBER
			SetGadgetText VERSIONLABEL, "Type: Tabber" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Items: " + gt\items$		;5
			AddGadgetItem GADGETPROPBOX, "Default tab: " + gt\defaultitem	;6
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;8

		Case PROGRESSBAR
			SetGadgetText VERSIONLABEL, "Type: Progressbar" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Progress: " + gt\progressval	;5
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable			;6
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$		;8
			AddGadgetItem GADGETPROPBOX, "Label: " + gt\autolabel$		;9

		Case HTML
			SetGadgetText VERSIONLABEL, "Type: HTMLview" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$				;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)			;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)			;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width				;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height				;4
			AddGadgetItem GADGETPROPBOX, "Default URL: " + gt\defaulturl$	;5
			; no enable
			; no group

		Case CANVAS
			SetGadgetText VERSIONLABEL, "Type: Canvas" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "ShowPointer: " + gt\pointershow	;5
			; no enable
			; no group

		Case TABPANEL
			SetGadgetText VERSIONLABEL, "Type: Tabpanel" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$					;0
			AddGadgetItem GADGETPROPBOX, "Belongs to tab item: " + gt\tabitem	;1

		Case PANEL
			SetGadgetText VERSIONLABEL, "Type: Panel" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4
			AddGadgetItem GADGETPROPBOX, "Border: " + gt\border		;5
			AddGadgetItem GADGETPROPBOX, "Background color: " + gt\r+","+gt\g+","+gt\b	;6

			If gt\image$=""
				txt$ = "none"
			Else
				txt$ = gt\image$
			EndIf
			AddGadgetItem GADGETPROPBOX, "Image: " + txt$			;7
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;8

		Case SLIDER
			SetGadgetText VERSIONLABEL, "Type: Slider" + append$
			AddGadgetItem GADGETPROPBOX, "Name: " + gt\name$		;0
			AddGadgetItem GADGETPROPBOX, "Xpos: " + Int(gt\xpos)	;1
			AddGadgetItem GADGETPROPBOX, "Ypos: " + Int(gt\ypos)	;2
			AddGadgetItem GADGETPROPBOX, "Width: " + gt\width		;3
			AddGadgetItem GADGETPROPBOX, "Height: " + gt\height		;4

			If gt\direction = 0
				txt$ = "Horizontal"
			Else
				txt$ = "Vertical"
			EndIf
			AddGadgetItem GADGETPROPBOX, "Direction: " + txt$		;5
			AddGadgetItem GADGETPROPBOX, "Enable: " + gt\enable		;6
			AddGadgetItem GADGETPROPBOX, "Group name: " + gt\group$	;7

		Case TOOLBAR

	End Select

	If gt\version <> WINDOW And gt\version <> TABPANEL  And gt\version <> FOLDBOX1
		Select gt\layoutleft
			Case 0
				lock$ = FREE$
			Case 1
				lock$ = LOCKED$
			Case 2
				lock$ = FLOATS$
		End Select
		AddGadgetItem GADGETPROPBOX, "Left alignment: " + lock$

		Select gt\layoutright
			Case 0
				lock$ = FREE$
			Case 1
				lock$ = LOCKED$
			Case 2
				lock$ = FLOATS$
		End Select
		AddGadgetItem GADGETPROPBOX, "Right alignment: " + lock$

		Select gt\layouttop
			Case 0
				lock$ = FREE$
			Case 1
				lock$ = LOCKED$
			Case 2
				lock$ = FLOATS$
		End Select
		AddGadgetItem GADGETPROPBOX, "Top alignment: " + lock$

		Select gt\layoutbottom
			Case 0
				lock$ = FREE$
			Case 1
				lock$ = LOCKED$
			Case 2
				lock$ = FLOATS$
		End Select
		AddGadgetItem GADGETPROPBOX, "Bottom alignment: " + lock$
	EndIf
End Function

Function SnapGadgetToGrid( gt.gtype )

	; snaps origin , width and height of passed gadget to grid

	SnapPosToGrid( gt\xpos, gt\ypos )
	gt\xpos = GR_ResultX
	gt\ypos = GR_ResultY

	SnapPosToGrid( gt\xpos + gt\width, gt\ypos )
	gt\width = GR_ResultX - gt\xpos

	SnapPosToGrid( gt\xpos, gt\ypos + gt\height )
	gt\height = GR_ResultY - gt\ypos

	; force height of the following gadgets

	If gt\version = COMBOBOX Or gt\version = TEXTFIELD Or gt\version = SPINNER
		gt\height = 20
	EndIf

	editor\changed = True

End Function

Function CornerHit( gt.gtype )

	; returns true if the passed coordinate is over the side handle of the passed gadget
	; the side handle is put in the variable editor\sidehandle

	; vertical scaling of gadget is restricted on : spinners, textfield, combobox

	Local x = editor\grid\cx
	Local y = editor\grid\cy

	; don't check side handles of the following gadgets

	If gt\hide = True Then Return False				; gadget is hidden
	If gt\version = IMAGE Then Return False			; size adjusts to image size

	; topleft

	If PointInRect( x,y, gt\xpos-6, gt\ypos-6, 6,6 )
		If gt\version <> TEXTFIELD And gt\version <> COMBOBOX And gt\version <> SPINNER And gt\version <> FOLDBOX1
			editor\sidehandle = MODE_MOVETL
			Return True
		EndIf
	EndIf

	; topright

	If PointInRect( x,y, gt\xpos + gt\width, gt\ypos-6, 6,6 )
		If gt\version <> TEXTFIELD And gt\version <> COMBOBOX And gt\version <> SPINNER And gt\version <> FOLDBOX1
			editor\sidehandle = MODE_MOVETR
			Return True
		EndIf
	EndIf

	; bottomleft

	If PointInRect( x,y, gt\xpos-6, gt\ypos + gt\height, 6,6 )
		If gt\version <> TEXTFIELD And gt\version <> COMBOBOX And gt\version <> SPINNER And gt\version <> FOLDBOX1
			editor\sidehandle = MODE_MOVEBL
			Return True
		EndIf
	EndIf

	; bottomright

	If PointInRect( x,y, gt\xpos + gt\width, gt\ypos + gt\height, 6,6 )
		If gt\version <> TEXTFIELD And gt\version <> COMBOBOX And gt\version <> SPINNER And gt\version <> FOLDBOX1
			editor\sidehandle = MODE_MOVEBR
			Return True
		EndIf
	EndIf

	; left

	If PointInRect( x,y, gt\xpos-6, gt\ypos+gt\height/2-3, 6,6 )
		If gt\version <> FOLDBOX1
			editor\sidehandle = MODE_MOVELEFT
			Return True
		EndIf
	EndIf

	; right

	If PointInRect( x,y, gt\xpos+gt\width, gt\ypos+gt\height/2-3, 6,6 )
		If gt\version <> FOLDBOX1
			editor\sidehandle = MODE_MOVERIGHT
			Return True
		EndIf
	EndIf

	; top

	If PointInRect( x,y, gt\xpos+gt\width/2-3, gt\ypos-6, 6,6 )
		If gt\version <> TEXTFIELD And gt\version <> COMBOBOX And gt\version <> SPINNER And gt\version <> FOLDBOX1
			editor\sidehandle = MODE_MOVETOP
			Return True
		EndIf
	EndIf

	; bottom

	If PointInRect( x,y, gt\xpos+gt\width/2-3, gt\ypos+gt\height, 6,6 )
		If gt\version <> TEXTFIELD And gt\version <> COMBOBOX And gt\version <> SPINNER
			editor\sidehandle = MODE_MOVEBOTTOM
			Return True
		EndIf
	EndIf

	; not over a corner

	Return False

End Function

Function DetermineStyle ( gt.gtype )

	; determines gadget style parameter based on values in gtype and gadget version
	; tool window and client coordinates are not yet supported

	Local style = 0

	Select gt\version
		Case WINDOW
			If gt\titlebar Then style = style + 1
			If gt\resize Then style = style + 2
			If gt\menu Then style = style + 4
			If gt\statusbar Then style = style + 8
			If gt\tool Then style = style + 16
			If gt\client Then style = style + 32

		Case BUTTON
			style = 1

		Case LABEL
			style = gt\border

		Case TEXTFIELD
			style = gt\mask

		Case TEXTAREA
			style = gt\wordwrap

		Case CHECKBOX
			style = 2

		Case RADIOBUTTON
			style = 3

		Case PANEL
			style = gt\border

		Case SLIDER
			style = gt\direction+1


;		Case LISTBOX		; not supported
;		Case COMBOBOX		; not supported
;		Case TREEVIEW		; not supported
;		Case TABBER			; not supported
;		Case TOOLBAR		; not supported
;		Case CANVAS			; not supported
;		Case PROGRESSBAR	; not supported

	End Select

	Return style

End Function

Function CheckConstraint( xmoved, ymoved, l.list )

	; check all gadgets and child gadgets in passed list and see if the passed movement
	; will make any hit a constraint. return true if this is the case

	While l <> Null
		If ListConstraint( xmoved, ymoved, l.list ) = True Then Return True
		l = l\nxt
	Wend

	Return False

End Function

Function ListConstraint( xmoved, ymoved, l.list )
	; see if items in this list hit a constraint. return true if yes.
	Local parent.gtype
	While l <> Null
		; use tabber dimensions if parent is a tabpanel

		parent = l\gt\parent
		If parent\version = TABPANEL Then parent = parent\parent

		; check if gadget hits parent boundaries after movement
		If l\gt\xpos+xmoved < parent\xpos
			If xmoved > 0 Then Return False			; allow moving 'inside'
			Return True
		EndIf

		If l\gt\xpos+l\gt\width+xmoved > parent\xpos+parent\width
			If xmoved < 0 Then Return False
			Return True
		EndIf
		If l\gt\ypos+ymoved < parent\ypos
			If ymoved > 0 Then Return False
			Return True
		EndIf
			
		If l\gt\ypos+l\gt\height+ymoved > parent\ypos+parent\height
			If ymoved < 0 Then Return False
			Return True
		EndIf

;		If l\gt\xpos+xmoved < parent\xpos Then Return True
;		If l\gt\xpos+l\gt\width+xmoved > parent\xpos+parent\width Then Return True
;		If l\gt\ypos+ymoved < parent\ypos Then Return True
;		If l\gt\ypos+l\gt\height+ymoved > parent\ypos+parent\height Then Return True

		; call again for possible children

		ListConstraint( xmoved, ymoved, l\gt\childlist )
		l = l\nxt
	Wend
	Return False
End Function

;#End Region