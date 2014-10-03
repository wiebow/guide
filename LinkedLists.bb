;
; GUIDE 1.x linked lists
;

;#Region definitions

; menu item linked list
Type Mlist
	Field prev.mlist
	Field mt.mtype		; menu items
	Field garbage		; flag.
	Field nxt.mlist
End Type

; gadgets linked list
Type List
	Field prev.list
	Field nxt.list
	Field gt.gtype		; gadgets
	;Field mt.mtype		; menu items
	Field garbage		; flag. if true
End Type
	
;#End Region

;#Region functions
	
Function DeleteList( li.list )

	; deletes gadgets in passed list
	; also deletes the list

	If li = Null Then Return

	; !!only delete emtpy tabpanels

	If li\gt\version = TABPANEL
		If li\gt\childlist <> Null Then Return	
	EndIf

	; first, flag each item and gadget in the list for deletion

	FlagList( li )

	; then get rid of the selection ist

	LL_ClearList( li )

	; run though remaining marked list items
	; which should only be members of childlists

	For l.list = Each list

		If l\garbage = True

			If l\prev = Null
				; there is no previous item. this has to be the beginning of a list

				If l\nxt = Null
					; this is also the last item in the list.
					; make sure parent knows this list is gone

					l\gt\parent\childlist = Null
				Else
					; there is a next item. as this item is disappearing, we should
					; tell the parent that the next item is now the first in his childlist

					l\gt\parent\childlist = l\nxt

					; tell that next item that there is no previous item

					l\nxt\prev = Null

				EndIf
			Else
				; there is a previous item....

				If l\nxt = Null
					; ...but no next item. so just tell previous item this one is gone

					l\prev\nxt = Null
				Else
					; ...and a next item. make previous item parent of next item so link is not broken

					l\nxt\prev = l\prev
					l\prev\nxt = l\nxt
				EndIf

			EndIf

			; ok. die

			Delete l

			editor\changed = True

		EndIf
	Next

	; then delete the gadget types

	For gt.gtype = Each gtype
		If gt\garbage = True

			; deleting a foldbox? then move the following foldboxes up

			If gt\version = FOLDBOX1

				distance = li\gt\height

				; find this foldbox in the parent childlist

				l.list = li\gt\parent\childlist

				While l\gt <> li\gt
					l = l\nxt
				Wend

				; we delete this foldbox, so
				; move the next foldbox according to this gadgets height

				MoveFoldBoxPosition( l\nxt, li\gt\height )

			EndIf

			; deleting a imagebox? free the image

			If gt\version = IMAGE Then FreeImage gt\imagehandle

			; delete the gadget

			Delete gt
		EndIf
	Next

	; clear the gadgetproperties listview

	HideInputGadgets()
	SetGadgetText VERSIONLABEL, "Type:"
	ClearGadgetItems GADGETPROPBOX
	SetStatusText MAINWINDOW, ""

End Function

Function FlagList( l.list )

	; marks each listitem and gadget in passed list for deletion

	Local l1.list

	While l <> Null

		If l\gt\childlist <> Null Then FlagList( l\gt\childlist )

		For l1 = Each list

			; find EVERY list item which contains this gadget and set the garbage flag

			If l1\gt = l\gt And l1\gt\garbage = False
				l1\garbage = True					; flag list item
				l1\gt\garbage = True				; flag gadget
				FreeTreeViewNode l1\gt\node			; get rid of node already
			EndIf
		Next

		l = l\nxt
	Wend

End Function

;;; <summary>Returns True if passed gadget is in editor selection list.</summary>
;;; <param name="gt">GUIDE gadget type</param>
;;; <remarks></remarks>
;;; <returns>True or False</returns>
;;; <subsystem>GUIDE.Lists</subsystem>
;;; <example>GadgetSelected( thisgadget )</example>
Function GadgetSelected( gt.gtype )
	Local l.list = editor\selection
	While l <> Null
		If l\gt = gt Then Return True
		l = l\nxt
	Wend
	Return False
End Function

Function LL_AddItem( gt.gtype, l.list, LLtype=LL_SELECTION )

	; adds a new item containing gadget to the end of passed list
	; passed list item should be the first in the list

	If l = Null

		; create first item if list does not exist
		; default new list is selection list

		Select LLtype
			Case LL_SELECTION
				editor\selection = New list
				editor\selection\gt = gt

			Case LL_CHILD
				gt\parent\childlist = New list
				gt\parent\childlist\gt = gt

		End Select

	Else

		Local prev.list

		; there is a list. find last item

		While l <> Null
			If l\gt = gt Then Return	; do not continue if gadget is already in list
			prev = l
			l = l\nxt
		Wend

		prev\nxt = New list					; add new list item
		prev\nxt\prev = prev				; store parent
		prev\nxt\gt = gt
	EndIf

	; store current position
	; this is needed if we move the gadgets of its parent, and the new parent is not valid

	gt\oldx = gt\xpos
	gt\oldy = gt\ypos

End Function

Function LL_ClearMenuList( ml.mlist )

	; clears passed linked list
	; passed list item should be the first in the list

	If ml = Null Then Return
	If ml = Null Then RuntimeError "Menu list does not exist!"

	While ml\nxt <> Null
		ml = ml\nxt
		Delete ml\prev
	Wend
	Delete ml

End Function

Function LL_AddItemMenu( mt.mtype, ml.mlist, LLtype=LL_SELECTION )

	; adds a new menu item containing gadget to the end of passed list
	; passed list item should be the first in the list

	If ml = Null

		; create first item if list does not exist
		; default new list is selection list

		Select LLtype
			Case LL_SELECTION
				editor\mselection = New mlist
				editor\mselection\mt = mt

			Case LL_CHILD
				mt\parent\childlist = New mlist
				mt\parent\childlist\mt = mt

		End Select

	Else

		Local prev.mlist

		; there is a list. find last item

		While ml <> Null
			If ml\mt = mt Then Return	; do not continue if gadget is already in list
			prev = ml
			ml = ml\nxt
		Wend

		prev\nxt = New mlist				; add new list item
		prev\nxt\prev = prev				; store parent
		prev\nxt\mt = mt
	EndIf

End Function

Function LL_ClearList( l.list )

	; clears passed linked list
	; passed list item should be the first in the list
	
	If l =Null Then Return
	;If l = Null Then RuntimeError "LL_ClearList: List does not exist!"
	
	While l\nxt <> Null
		l = l\nxt
		Delete l\prev
	Wend
	Delete l

End Function

;Function LL_CopyList( s.list, d.list )
;
;	; copies source list to new destination list.
;
;	If s = Null Then RuntimeError " list not present!"
;
;	d = New list
;	d\gt = s\gt
;
;	While s\nxt <> Null
;
;		d\nxt = New list	; source list has one more item, so create new destination item
;		d\nxt\prev = d		; store parent of new item
;
;		s = s\nxt			; go to next source item
;		d = d\nxt			; go to new destination item
;
;		s\gt = d\gt			; store link to source gadget
;	Wend
;
;End Function

;Function LL_PasteList( s.list, d.list )
;
;	; creates new versions of the gadgets contained in the source list
;	; into a new destination list. it will also re-create all child gadgets of the source gadgets
;
;	If editor\selection = Null Then RuntimeError "no destination selected!"
;	If s = Null Then RuntimeError "source does not exist!"
;
;	d = New list
;	d\gt = New gtype
;
;	While s <> Null
;
;		; create gadget and copy settings
;
;		UpdateCounter( s\gt\version )
;		d\gt = CreateGadget( s\gt\version )
;		CopyGadgetSettings( s\gt, d\gt )
;
;		; do possible children
;
;		If s\gt\childlist <> Null Then LL_PasteList( s\gt\childlist, d\gt\childlist )
;
;		; check if a next gadget exists
;
;		s = s\nxt
;
;		If s <> Null
;			d\nxt = New list		; create next item
;			d\nxt\prev = d			; store this item as parent
;			d = d\nxt				; go there
;		EndIf
;
;	Wend
;
;End Function


Function HideSelection( l.list )

	; hides passed list, and children in that list
	; can be gadgets or menu items in list

	While l <> Null
		l\gt\hide = True
		If l\gt\childlist <> Null Then HideSelection( l\gt\childlist )
		l = l\nxt
	Wend

End Function

Function UnhideSelection( l.list )

	; unhides passed list

	While l <> Null
		l\gt\hide = False
		If l\gt\childlist <> Null Then UnhideSelection( l\gt\childlist )
		l = l\nxt
	Wend

End Function

;#End Region