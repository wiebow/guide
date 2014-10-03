;
; GUIDE 1.x menu funcions
;

Function CreateMenuItem.mtype()

	; creates a menu item.

	; enable menu bar if there is none there yet, and add rootmt to editor\mselection

	If editor\rootmt\childlist = Null
		editor\rootgt\menu = True
		SetGadgetShape DISPLAYCANVAS, 0,19, GadgetWidth( DISPLAYCANVAS) , GadgetHeight( DISPLAYCANVAS )
		ShowGadget MENUCANVAS

		LL_AddItemMenu( editor\rootmt, editor\mselection )

	EndIf

	If editor\mselection <> Null

		parent.mtype = editor\mselection\mt

		; create menu item

		mt.mtype = New mtype
		mt\version = MENUITEM
		mt\parent = parent
		mt\id = editor\menuidcount
		mt\label$ = "MenuItem" + editor\menuidcount
		mt\shortcut$ = ""
		mt\active = True
		mt\checkable = False
		mt\checked = False

		; add new item to childlist of parent

		LL_AddItemMenu( mt, mt\parent\childlist, LL_CHILD )

		; add menu item to gadget tree

		mt\node = AddTreeViewNode ( mt\label$, parent\node, MENUROOT )
		ExpandTreeViewNode parent\node

		UpdateCounter( MENUITEM )
		editor\changed = True
	EndIf

End Function

Function DeleteMenuItems()

	; deletes selected gadgets

	; first, flag each item and gadget in the list for deletion
	FlagMenuList( editor\mselection )

	; then get rid of the selection list
	LL_ClearMenuList( editor\mselection )

	; run though remaining list items
	; which should only be members of childlists and delete the marked items

	For ml.mlist = Each mlist

		If ml\garbage = True

			If ml\prev = Null
				; there is no previous item. this has to be the beginning of a list

				If ml\nxt = Null
					; this is also the last item in the list.
					; make sure parent knows this list is gone

					ml\mt\parent\childlist = Null
				Else
					; there is a next item. as this item is disappearing, we should
					; tell the parent that the next item is now the first in his childlist

					ml\mt\parent\childlist = ml\nxt

					; tell that next item that there is no previous item

					ml\nxt\prev = Null

				EndIf
			Else
				; there is a previous item....

				If ml\nxt = Null
					; ...but no next item. so just tell previous item this one is gone

					ml\prev\nxt = Null
				Else
					; ...and a next item. make previous item parent of next item so link is not broken

					ml\nxt\prev = ml\prev
					ml\prev\nxt = ml\nxt
				EndIf

			EndIf

			; ok. die

			Delete ml

			editor\changed = True

		EndIf
	Next

	; then delete the menu items

	For mt.mtype = Each mtype
		If mt\garbage = True Then Delete mt
	Next

	; get rid of menu bar if all menu items are gone. (and menu option is not checked)

	If editor\rootmt\childlist = Null And editor\rootgt\menu = False
		SetGadgetShape DISPLAYCANVAS, 0,0, GadgetWidth(DISPLAYCANVAS) , GadgetHeight( DISPLAYCANVAS )
		HideGadget MENUCANVAS
	EndIf

	; clear the gadgetproperties listview

	HideInputGadgets()
	SetGadgetText VERSIONLABEL, "Type:"
	ClearGadgetItems GADGETPROPBOX
	SetStatusText MAINWINDOW, ""

End Function

Function FlagMenuList( ml.mlist )

	; marks each listitem and gadget in the passed list for deletion

	Local ml1.mlist

	While ml <> Null

		If ml\mt\version <> MENUROOT			; NEVER flag a menu root for deletion

			If ml\mt\childlist <> Null Then FlagMenuList( ml\mt\childlist )

			For ml1 = Each mlist

				; find EVERY list item which contains this gadget and set the garbage flag

				If ml1\mt = ml\mt And ml1\mt\garbage = False
					ml1\garbage = True					; flag list item
					ml1\mt\garbage = True				; flag gadget
					FreeTreeViewNode ml1\mt\node		; get rid of node already
				EndIf
			Next

		EndIf

		ml = ml\nxt
	Wend

End Function

Function ShowMenuDetails( mt.mtype )

	; shows menu details in the GADGETPROPBOX

;	If delinput = True Then HideInputGadgets()
	ClearGadgetItems GADGETPROPBOX

	Select mt\version
		Case MENUROOT
			SetGadgetText VERSIONLABEL, "Type: Menu ROOT"
			AddGadgetItem GADGETPROPBOX, "Name: " + mt\label$

			SetStatusText MAINWINDOW, "CTRL-TAB to add item"

		Case MENUITEM
			SetGadgetText VERSIONLABEL, "Type: Menu Item"

			AddGadgetItem GADGETPROPBOX, "Text: " + mt\label$
			AddGadgetItem GADGETPROPBOX, "Active: " + mt\active

			If mt\parent\version <> MENUROOT
				AddGadgetItem GADGETPROPBOX, "Can be checked: " + mt\checkable
				AddGadgetItem GADGETPROPBOX, "Checked: " + mt\checked
			EndIf

;			AddGadgetItem GADGETPROPBOX, "Shortcut: " + mt\shortcut$

;			Select mt\shortcutmod
;				Case 0
;					txt$ = ""
;				Case 1
;					txt$ = "SHIFT"
;				Case 2
;					txt$ = "CTRL"
;				Case 3
;					txt$ = "ALT"
;			End Select
;			AddGadgetItem GADGETPROPBOX, "Shortcut modifier: " + txt$

	End Select

End Function

Function CreateMenuPropInput( item )

	; show the correct input gadget for the selected menu and property

	HideInputGadgets()

;	ClearGadgetItems MENUCOMBO
;	SetStatusText MAINWINDOW, ""

	; store selected item
	editor\activeprop = item

	Local mt.mtype = editor\mselection\mt

	Select mt\version

		Case MENUROOT
			ShowGadget MENUTXT
			SetGadgetText MENUTXT, mt\label$
			ActivateGadget MENUTXT
			SelectAllText( MENUTXT )

		Case MENUITEM
			Select item
				Case 0
					SetGadgetText MENUTXT, mt\label$
					ActivateGadget MENUTXT
					ShowGadget MENUTXT
					SelectAllText( MENUTXT )
				Case 1
					SetGadgetText MENUCHECK, "Active"
					SetButtonState MENUCHECK, mt\active
					ShowGadget MENUCHECK
				Case 2
					SetGadgetText MENUCHECK, "Can be checked"
					SetButtonState MENUCHECK, mt\checkable
					ShowGadget MENUCHECK
				Case 3
					SetGadgetText MENUCHECK, "Checked"
					SetButtonState MENUCHECK, mt\checked
					ShowGadget MENUCHECK
;				Case 4
;					ShowGadget SHORTCUT
;					SetGadgetText MENUTXT, mt\shortcut$
;					ActivateGadget MENUTXT
;				Case 5
;					AddGadgetItem MENUCOMBO, ""
;					AddGadgetItem MENUCOMBO, "SHIFT"
;					AddGadgetItem MENUCOMBO, "CTRL"
;					AddGadgetItem MENUCOMBO, "ALT"
;					SelectGadgetItem MENUCOMBO, mt\shortcutmod
;					ShowGadget MENUCOMBO

			End Select

	End Select

End Function

Function MoveMenu( dir )

; move menu item according to passed direction. -1 = up, 1 = down

	Local c.mlist = editor\rootmt\childlist

	; first, find the selected menu item we want to move
	; we can only move menus which are children of the root, so check rootmt childlist and see if
	;	that holds the selected menu item.

	While c <> Null
		If c\mt = editor\mselection\mt

			; found! the menu is mselection is a child of the root so we can move it
			; check the direction to move the menu item in

			If dir < 0 ; then move up.

				; see if menuitem is not first in list

				If c\prev <> Null

					; this item is not the first in the list. move it 'up'

					this.mtype = c\mt
					thislabel$ = this\label$

					; swap list items

					c\mt = c\prev\mt
					c\prev\mt = this

					; select moved item

					SelectTreeViewNode c\prev\mt\node

					; show changes

					UpdateCanvas( MENUCANVAS ) : Return

				EndIf
			Else		; move right

				If c\nxt <> Null

					; swap menuitems in linked list list types

					this.mtype = c\mt
					thislabel$ = this\label$

					; swap list items

					c\mt = c\nxt\mt
					c\nxt\mt = this

					; select moved item

					SelectTreeViewNode c\nxt\mt\node

					; show changes

					UpdateCanvas( MENUCANVAS ) : Return

				EndIf

			EndIf

		EndIf

		c = c\nxt

	Wend

End Function
