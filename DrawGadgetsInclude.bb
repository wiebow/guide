;INCLUDE

; draws GUIde gadgets

Function GAD_Progress( gt.gtype )

	; draws a progressbar

	Color editor\gadr,editor\gadg,editor\gadb
	Rect gt\xpos,gt\ypos, gt\width,gt\height, 1

	If gt\enable
		Color editor\sel_text_r, editor\sel_text_g, editor\sel_text_b
	Else
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	Rect gt\xpos,gt\ypos, Int(gt\width * gt\progressval), gt\height

	Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
	Line gt\xpos,gt\ypos+gt\height, gt\xpos+gt\width, gt\ypos+gt\height
	Line gt\xpos+gt\width, gt\ypos, gt\xpos+gt\width, gt\ypos+gt\height

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Line gt\xpos,gt\ypos,gt\xpos+gt\width,gt\ypos
	Line gt\xpos,gt\ypos, gt\xpos, gt\ypos+gt\height

	If gt\autolabel$ <> ""
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
		Text gt\xpos - 4 - StringWidth( gt\autolabel$ ) , gt\ypos, gt\autolabel$
	EndIf

End Function

Function GAD_Button( gt.gtype )

	; draws a button

	GAD_Box( gt\xpos, gt\ypos,gt\width, gt\height, gt\enable, 0 )

	Local label$ = FitStringToWidth( gt\label$, gt\width )

	x = gt\width / 2 - StringWidth( label$ ) / 2
	y = gt\height / 2 - StringHeight( label$ ) / 2

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
		Text gt\xpos + x + 1, gt\ypos + y + 1, label$
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	Text gt\xpos + x, gt\ypos + y, label$

End Function

Function GAD_Label( gt.gtype )

	; draws a label

	Color editor\gadr,editor\gadg,editor\gadb
	Rect gt\xpos, gt\ypos, gt\width, gt\height, 1

	Select gt\border
		Case 0,2			; no border

		Case 1				; flat border
			Color editor\win_text_r, editor\win_text_g, editor\win_text_b
			Rect gt\xpos,gt\ypos, gt\width, gt\height, 0

		Case 3				; sunken border
			GAD_Box( gt\xpos, gt\ypos,gt\width, gt\height, 0, 1 )

	End Select

	Local label$ = FitStringToWidth( gt\label$, gt\width )

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
		Text gt\xpos + x+2, gt\ypos + y+2, label$
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf
	Text gt\xpos + 3, gt\ypos + 1, label$

End Function

Function GAD_CheckBox( gt.gtype )

	; draws a checkbox

	y = gt\height / 2 - StringHeight( gt\label$ ) / 2
	GAD_BOX( gt\xpos, gt\ypos+y, 13,13, gt\enable, 1 )

	; check itself

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	If gt\checked
		Line gt\xpos+3, gt\ypos+y+5, gt\xpos+3, gt\ypos+y+8
		Line gt\xpos+4, gt\ypos+y+6, gt\xpos+4, gt\ypos+y+9
		Line gt\xpos+5, gt\ypos+y+7, gt\xpos+5, gt\ypos+y+10
		Line gt\xpos+6, gt\ypos+y+6, gt\xpos+6, gt\ypos+y+9
		Line gt\xpos+7, gt\ypos+y+5, gt\xpos+7, gt\ypos+y+8
		Line gt\xpos+8, gt\ypos+y+4, gt\xpos+8, gt\ypos+y+7
		Line gt\xpos+9, gt\ypos+y+3, gt\xpos+9, gt\ypos+y+6
	EndIf

	Local label$ = FitStringToWidth( gt\label$, gt\width )

	; text

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
		Text gt\xpos + 19, gt\ypos+y+1, label$
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	Text gt\xpos+18, gt\ypos + y, label$

End Function

Function GAD_RadioButton( gt.gtype )

	y = gt\height / 2 - StringHeight( gt\label$ ) / 2

	; circle

	Color editor\winr, editor\wing, editor\winb
	Oval gt\xpos, gt\ypos+y, 12,12
	Color editor\gadr,editor\gadg,editor\gadb
	Oval gt\xpos+1, gt\ypos+y+1, 10,10

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Line gt\xpos+4, gt\ypos+y, gt\xpos+7, gt\ypos+y
	Line gt\xpos+2, gt\ypos+y+1, gt\xpos+9, gt\ypos+y+1
	Line gt\xpos,gt\ypos+y+4, gt\xpos,gt\ypos+y+7
	Line gt\xpos+1, gt\ypos+y+2, gt\xpos+1, gt\ypos+y+9

	Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
	Line gt\xpos+4, gt\ypos+y+1, gt\xpos+7, gt\ypos+y+1
	Line gt\xpos+2, gt\ypos+y+2, gt\xpos+9,gt\ypos+y+2
	Line gt\xpos+1, gt\ypos+y+4, gt\xpos+1, gt\ypos+y+7
	Line gt\xpos+2, gt\ypos+y+2, gt\xpos+2, gt\ypos+y+9

	If gt\enable
		Color editor\winr, editor\wing, editor\winb
	Else
		Color editor\gadr,editor\gadg,editor\gadb
	EndIf
	Oval gt\xpos+2, gt\ypos+y+2, 8,8,1

	; check dot
	If gt\checked
		If gt\enable
			Color editor\win_text_r, editor\win_text_g, editor\win_text_b
		Else
			Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
		EndIf
		Rect gt\xpos +4, gt\ypos+y+4, 4,4,1
	EndIf

	; text

	Local label$ = FitStringToWidth( gt\label$, gt\width )

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
		Text gt\xpos + 19, gt\ypos+y+1, label$
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	Text gt\xpos + 18, gt\ypos + y, label$

End Function

Function GAD_TextField( gt.gtype )

	; draws a text field

	GAD_Box( gt\xpos, gt\ypos,gt\width, gt\height, gt\enable, 1 )

	y = gt\height / 2 - StringHeight( gt\defaultstring$ ) / 2

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	If gt\mask
		lbl$ = FitStringToWidth( String$("*",Len(gt\defaultstring) ), gt\width - 4 )
		Text gt\xpos+4, gt\ypos+y, lbl$; String$("*",Len(gt\defaultstring) )
	Else
		lbl$ = FitStringToWidth( gt\prefix$ + gt\defaultstring$ + gt\suffix$, gt\width - 4 )
		Text gt\xpos+4, gt\ypos + y, lbl$
	EndIf

	If gt\autolabel$ <> "" Then Text gt\xpos - 4 - StringWidth( gt\autolabel$ ) , gt\ypos + 4, gt\autolabel$

End Function

Function GAD_TextArea( gt.gtype )

	; draws a text area

	If gt\enable
		Color gt\r, gt\g, gt\b
	Else
		Color editor\gadr, editor\gadg, editor\gadb
	EndIf
	Rect gt\xpos,gt\ypos, gt\width, gt\height

	;border
	xpos = gt\xpos
	ypos=gt\ypos
	width=gt\width
	height=gt\height

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Line xpos,ypos, xpos+width, ypos
	Line xpos,ypos, xpos, ypos+height

	Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
	Line xpos,ypos+height, xpos+width, ypos+height
	Line xpos+width, ypos, xpos+width, ypos+height

	Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
	Line xpos+1, ypos+1, xpos+width-1, ypos+1
	Line xpos+1, ypos+1, xpos+1, ypos+height-1

	Color editor\gadr,editor\gadg,editor\gadb
	Line xpos+1, ypos+height-1, xpos+width-1, ypos+height-1
	Line xpos+width-1, ypos+1, xpos+width-1, ypos+height-1

	;text

	lbl$ = FitStringToWidth( gt\defaultstring$, gt\width - 4 )

	Color gt\textr, gt\textg, gt\textb
	Text gt\xpos+4, gt\ypos + 4, lbl;gt\defaultstring$

End Function

Function GAD_HTMLview( gt.gtype )

	; draws a html area

	GAD_Box( gt\xpos, gt\ypos,gt\width, gt\height, gt\enable, 1 )

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	lbl$ = FitStringToWidth( gt\defaulturl, gt\width - 4 )

	Text gt\xpos+4, gt\ypos + 4, lbl$;gt\defaulturl

End Function

Function GAD_ListBox( gt.gtype )

	; draws a listbox

	GAD_Box( gt\xpos, gt\ypos,gt\width, gt\height, gt\enable, 1 )

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	items$ = gt\items$
	Local y = 4
	If Right$( items$, 1 ) <> ";" Then items$ = items$ + ";"

	For count = 1 To Len( items$ )

		If gt\ypos+ y + 14 > gt\ypos + gt\height Then Exit


		; write each item

		If Mid$( items$, count, 1 ) = ";"

			lastsep = count

			txt$ = FitStringToWidth( Mid$( items$, laststart, lastsep-laststart ), gt\width - 4 )
			laststart = count + 1

			Text gt\xpos + 4, gt\ypos+ y, txt$
			y = y + 14

		EndIf

	Next

End Function

Function GAD_Tabber( gt.gtype )

	; draws a tabber

	; main box

	Local xpos = gt\xpos
	Local ypos = gt\ypos + 20
	Local width = gt\width
	Local height = gt\height - 20

	Color editor\gadr,editor\gadg,editor\gadb
	Rect gt\xpos, gt\ypos, gt\width, 20,1;gt\height, 1

	Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
	Line xpos, ypos, xpos, ypos+height
	Line xpos, ypos, xpos+width, ypos

	Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
	Line xpos,ypos+height, xpos+width, ypos+height
	Line xpos+width, ypos, xpos+width, ypos+height

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Line xpos+1, ypos+height-1, xpos+width-1, ypos+height-1
	Line xpos+width-1, ypos+1, xpos+width-1, ypos+height-1

	Local laststart = 1
	Local lastsep = 0
	Local tabcount = 0

	items$ = gt\items$
	If Right$( items$, 1 ) <> ";" Then items$ = items$ + ";"

	; draw each item

	For count = 1 To Len( items$ )

		If Mid$( items$, count, 1 ) = ";"
			lastsep = count
			txt$ = Mid$( items$, laststart, lastsep-laststart )
			laststart = count + 1

			; determine width of item
			width = StringWidth( txt$ ) + 20

			; determine height of item. active item is drawn slighty larger

			If tabcount = gt\defaultitem
				yoffset= 0

				; get rid of white line underneath active item
				Color editor\gadr,editor\gadg,editor\gadb
				Line xpos,ypos, xpos+width, ypos
			Else
				yoffset= 2
			EndIf

			tabcount = tabcount + 1

			; item  border

			Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
			Line xpos, gt\ypos + yoffset, xpos+width, gt\ypos + yoffset
			Line xpos, gt\ypos + yoffset, xpos, gt\ypos + 19

			Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
			Line xpos+width-1, gt\ypos+yoffset+1, xpos+width-1, gt\ypos+19

			; select text color

			If gt\enable
				Color editor\win_text_r, editor\win_text_g, editor\win_text_b
			Else
				Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
			EndIf

			; draw label name
			txtoffset = width / 2 - StringWidth( txt$ ) / 2
			Text xpos + txtoffset, gt\ypos+yoffset+2, txt$

			xpos = xpos + width

		EndIf
	Next

	; hide/show tabber panels

	ShowTabberPanel( gt )

End Function

Function GAD_ComboBox( gt.gtype )

	; draws a combo box

	; draw main box
	GAD_Box( gt\xpos,gt\ypos, gt\width, 20, gt\enable, 1 )

	; draw default item

	itemcount = 0
	lastsep=1
	items$ = gt\items$
	If Right$( items$, 1 ) <> ";" Then items$ = items$ + ";"

	For count = 1 To Len( items$ )
		If Mid$( items$, count, 1 ) = ";"
			If itemcount = gt\defaultitem
				txt$ = FitStringToWidth( Mid$( items$, lastsep, count-lastsep ), gt\width - 4 ); Mid$( items$, lastsep, count-lastsep )
				Exit
			EndIf
			lastsep = count+1
			itemcount = itemcount+1
		EndIf
	Next

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	Text gt\xpos + 4, gt\ypos + 4, txt$

	; draw arrow box
	GAD_Box( gt\xpos + gt\width - 18, gt\ypos + 2, 16, 16, gt\enable, 0 )

	; draw arrow
	GAD_Arrow( gt\xpos+ gt\width - 13, gt\ypos + 8, gt\enable, 1 )

	If gt\autolabel$ <> "" Then Text gt\xpos - 4 - StringWidth( gt\autolabel$ ) , gt\ypos + 4, gt\autolabel$

End Function

Function GAD_Box( xpos,ypos,width,height, enable, mode )

	; draws a box

	Select mode
		Case 0
			; draw raised box

			Color editor\gadr,editor\gadg,editor\gadb
			Rect xpos, ypos, width, height, 1

			Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
			Line xpos, ypos, xpos + width, ypos
			Line xpos, ypos, xpos, ypos + height

			Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
			Line xpos, ypos + height, xpos + width, ypos + height
			Line xpos + width, ypos, xpos + width, ypos + height

			Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
			Line xpos + 1, ypos + height - 1, xpos + width - 1, ypos + height-1
			Line xpos + width - 1, ypos + 1, xpos + width - 1, ypos + height - 1

		Case 1
			; draw lowered box

			If enable
				Color editor\winr, editor\wing, editor\winb
			Else
				Color editor\gadr,editor\gadg,editor\gadb
			EndIf
			Rect xpos, ypos, width, height, 1

			Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
			Line xpos,ypos, xpos+width, ypos
			Line xpos,ypos, xpos, ypos+height

			Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
			Line xpos,ypos+height, xpos+width, ypos+height
			Line xpos+width, ypos, xpos+width, ypos+height

			Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
			Line xpos+1, ypos+1, xpos+width-1, ypos+1
			Line xpos+1, ypos+1, xpos+1, ypos+height-1

			Color editor\gadr,editor\gadg,editor\gadb
			Line xpos+1, ypos+height-1, xpos+width-1, ypos+height-1
			Line xpos+width-1, ypos+1, xpos+width-1, ypos+height-1

	End Select

End Function

Function GAD_Panel( gt.gtype )

	; draws a panel

	; drawing will be clipped if the panel is larger than its parent

	Local parent.gtype = gt\parent
	Local xpos,ypos, width,height

	xpos = gt\xpos							; get draw dimensions
	ypos = gt\ypos
	width = gt\width
	height = gt\height

	; check for oversize, and clip panel if necessary

	If gt\width > parent\width And parent\version <> TABPANEL
		width = parent\width
		gt\xpos = parent\xpos				; move panel there for real as well

		parent\largechildx = True			; notify parent it holds a larger child
	Else
		parent\largechildx = False
	EndIf

	If gt\height > parent\height And parent\version <> TABPANEL
		height = parent\height
		gt\ypos = parent\ypos				; move panel there for real as well

		parent\largechildy = True			; notify parent of its large child
	Else
		parent\largechildy = False
	EndIf

	; draw grey outline

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Rect gt\xpos,gt\ypos, gt\width,gt\height,0

	; draw border

	If gt\border

		xpos = gt\xpos			; get real dimensions
		ypos = gt\ypos
		width = gt\width
		height = gt\height

		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
		Line xpos,ypos, xpos+width, ypos
		Line xpos,ypos, xpos, ypos+height

		Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
		Line xpos,ypos+height, xpos+width, ypos+height
		Line xpos+width, ypos, xpos+width, ypos+height

		Color editor\gad_dshade_r, editor\gad_dshade_g, editor\gad_dshade_b
		Line xpos+1, ypos+1, xpos+width-1, ypos+1
		Line xpos+1, ypos+1, xpos+1, ypos+height-1

		Color editor\gadr,editor\gadg,editor\gadb
		Line xpos+1, ypos+height-1, xpos+width-1, ypos+height-1
		Line xpos+width-1, ypos+1, xpos+width-1, ypos+height-1

	EndIf

	; draw backdrop image here

;	If gt\image$ <> ""
;
;	EndIf

	; show label on panel

	If editor\showlabels
		Color 0,0,0
		x = width / 2 - StringWidth( "panel" ) / 2
		y = height / 2 - StringHeight( "panel" ) / 2
		Text xpos+x,ypos+y, gt\name$
	EndIf

	; draw sliders for parent if this panel is larger

	If parent\largechildx = True

		; get dimension and location of parent

		xpos = parent\xpos
		ypos = parent\ypos + parent\height + editor\grid\size
		width = parent\width
		height = 16			; default slider height

		; draw slider

		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
		Rect xpos, ypos, width, height, 1

		y = height / 2 - 3
		GAD_Box( xpos, ypos, 16, height, 1, 0 )
		GAD_Arrow( xpos + 6, ypos + y, 1, 3 )
		GAD_Box( xpos+width-16, ypos, 16, height, 1, 0 )
		GAD_Arrow( xpos+width - 10, ypos+ y, 1, 4 )

	EndIf

	If parent\largechildy = True

		xpos = parent\xpos + parent\width + editor\grid\size
		ypos = parent\ypos
		width = 16
		height = parent\height

		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
		Rect xpos, ypos, width, height, 1

		x = width / 2 - 3
		GAD_Box( xpos, ypos, width, 16, 1, 0 )
		GAD_Arrow( xpos + x, ypos + 6, 1, 2 )
		GAD_Box( xpos, ypos+height-16, width, 16, 1, 0 )
		GAD_Arrow( xpos + x, ypos+height-16+6, 1, 1 )

	EndIf

End Function

Function GAD_Slider( gt.gtype )

	; draws a slider

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Rect gt\xpos, gt\ypos, gt\width+1, gt\height+1, 1

	Select gt\direction
		Case 0 						; horizontal
			y = gt\height / 2 - 3
			GAD_Box( gt\xpos, gt\ypos, 16, gt\height, gt\enable, 0 )
			GAD_Arrow( gt\xpos + 6, gt\ypos + y, gt\enable, 3 )
			GAD_Box( gt\xpos+gt\width-16, gt\ypos, 16, gt\height, gt\enable, 0 )
			GAD_Arrow( gt\xpos+gt\width - 10, gt\ypos+ y, gt\enable, 4 )

		Case 1						; vertical
			x = gt\width / 2 - 3
			GAD_Box( gt\xpos, gt\ypos, gt\width, 16, gt\enable, 0 )
			GAD_Arrow( gt\xpos + x, gt\ypos + 6, gt\enable, 2 )
			GAD_Box( gt\xpos, gt\ypos+gt\height-16, gt\width, 16, gt\enable, 0 )
			GAD_Arrow( gt\xpos + x, gt\ypos+gt\height-16+6, gt\enable, 1 )

	End Select

End Function

Function GAD_Groupbox( gt.gtype )

	; draws a groupbox

	xpos = gt\xpos
	ypos=gt\ypos
	width=gt\width
	height=gt\height

	; border
	Color editor\gadr,editor\gadg,editor\gadb
	Rect xpos,ypos,width,16;height

	Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
	Rect xpos,ypos+6,width,height-6, 0
	Rect xpos+1,ypos+7,width-2,height-8, 0

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Rect xpos,ypos+6,width-1,height-7, 0

	; text
	Color editor\gadr,editor\gadg,editor\gadb
	Rect gt\xpos+4, gt\ypos, StringWidth( gt\label$ )+8, StringHeight( gt\label$ ), 1

	If gt\enable
		Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Else
		Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
		Text xpos + 9, ypos + 1, gt\label$
		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	EndIf

	Text xpos+8, ypos, gt\label$

End Function

Function GAD_Line( gt.gtype )

	; draws a seperator-line

	xpos = gt\xpos
	ypos=gt\ypos
	width=gt\width
	height=gt\height

	Color editor\gadr,editor\gadg,editor\gadb
	Rect xpos,ypos,width,height

	Select gt\direction
		Case 0						; horizontal
			ypos = ypos + gt\height /2
			Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
			Line xpos,ypos, xpos+width, ypos
			Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
			Line xpos,ypos+1, xpos+width, ypos+1

			If gt\label$ <> ""		; then draw label
				Color editor\gadr,editor\gadg,editor\gadb
				Rect xpos+4, ypos- StringHeight( gt\label$) / 2, StringWidth( gt\label$ )+8, StringHeight( gt\label$ ), 1
				Color editor\win_text_r, editor\win_text_g, editor\win_text_b
				Text xpos+8, ypos- StringHeight( gt\label$) / 2, gt\label$
			EndIf

		Case 1						; vertical. no label
			xpos = xpos + width /2
			Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
			Line xpos, ypos, xpos, ypos+height
			Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
			Line xpos+1, ypos, xpos+1, ypos+height

	End Select

End Function

Function GAD_Arrow( xpos,ypos, enable, direction )

	; draws a little arrow
	; for sliders, comboboxes, etc

	Select enable
		Case 0
			Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
		Case 1
			Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	End Select

	Select direction
		Case 1								; down
			Line xpos, ypos, xpos+6, ypos
			Line xpos+1, ypos+1, xpos+5, ypos+1
			Line xpos+2, ypos+2, xpos+4, ypos+2
			Plot xpos+3, ypos+3

		Case 2								; up
			Plot xpos+3, ypos
			Line xpos+2, ypos+1, xpos+4, ypos+1
			Line xpos+1, ypos+2, xpos+5, ypos+2
			Line xpos, ypos+3, xpos+6, ypos + 3

		Case 3								; left
			Plot xpos, ypos+3
			Line xpos+1, ypos+2, xpos+1, ypos+4
			Line xpos+2, ypos+1, xpos+2, ypos+5
			Line xpos+3, ypos, xpos+3, ypos+6

		Case 4								; right
			Line xpos, ypos, xpos, ypos+6
			Line xpos+1, ypos+1, xpos+1, ypos+5
			Line xpos+2, ypos+2, xpos+2, ypos+4
			Plot xpos+3, ypos+3

	End Select

End Function

Function GAD_Foldbox( gt.gtype )

	; draw a foldable groupbox. in GUIde, these are always drawn open.

	Local xpos = gt\xpos + 2	; compensate for panel border, just for drawing purposes
	Local ypos = gt\ypos + 2
	Local width = gt\width - 4
	Local height = gt\height - 2

	Color editor\gad_hilite_r, editor\gad_hilite_g, editor\gad_hilite_b
	Rect xpos+1,ypos+6, width-2,height-6, 0
	Rect xpos+2,ypos+7, width-3,height-8, 0
	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Rect xpos+1,ypos+6, width-3,height-7, 0

	Color editor\gadr-30,editor\gadg-30,editor\gadb-30
	Rect xpos+6,ypos, width-12, StringHeight( gt\label$ ), 1
	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Rect xpos+6,ypos, width-12, StringHeight( gt\label$ ), 0

	Color editor\win_text_r, editor\win_text_g, editor\win_text_b
	Text xpos+10,ypos, "- " + gt\label$

End Function

Function GAD_Canvas( gt.gtype )

	; check if canvas is larger than its parent, clip size if necessary

	Local parent.gtype = gt\parent
	Local xpos,ypos, width,height

	xpos = gt\xpos
	ypos = gt\ypos
	width = gt\width
	height = gt\height

	If gt\width > parent\width And parent\version <> TABPANEL
		width = parent\width
		xpos = parent\xpos
		gt\xpos = xpos						; move gadget there for real as well
		parent\largechildx = True
	Else
		parent\largechildx = False
	EndIf

	If gt\height > parent\height And parent\version <> TABPANEL
		height = parent\height
		ypos = parent\ypos
		gt\ypos = ypos						; move gadget there for real as well
		parent\largechildy = True
	Else
		parent\largechildy = False
	EndIf

	; draw grey outline if panel is larger than its parent

	If parent\largechildy Or parent\largechildx
		Color editor\gadr-15,editor\gadg-15,editor\gadb-15
 		Rect gt\xpos,gt\ypos, gt\width,gt\height,0			; use real dimension, not draw dimension
	EndIf

	; draw the canvas

	Color 0,0,0
	Rect xpos, ypos, width, height, 1

	If editor\showlabels
		Color 255,255,255
		x = width / 2 - StringWidth( "canvas" ) / 2
		y = height / 2 - StringHeight( "canvas" ) / 2
		Text xpos+x,ypos+y,"canvas"
	EndIf

	; draw sliders for parent if this panel is larger

	If parent\largechildx = True

		; get dimension and location of parent

		xpos = parent\xpos
		ypos = parent\ypos + parent\height + editor\grid\size
		width = parent\width
		height = 16			; default slider height

		; draw slider

		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
		Rect xpos, ypos, width, height, 1

		y = height / 2 - 3
		GAD_Box( xpos, ypos, 16, height, 1, 0 )
		GAD_Arrow( xpos + 6, ypos + y, 1, 3 )
		GAD_Box( xpos+width-16, ypos, 16, height, 1, 0 )
		GAD_Arrow( xpos+width - 10, ypos+ y, 1, 4 )

	EndIf

	If parent\largechildy = True

		xpos = parent\xpos + parent\width + editor\grid\size
		ypos = parent\ypos
		width = 16
		height = parent\height

		Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
		Rect xpos, ypos, width, height, 1

		x = width / 2 - 3
		GAD_Box( xpos, ypos, width, 16, 1, 0 )
		GAD_Arrow( xpos + x, ypos + 6, 1, 2 )
		GAD_Box( xpos, ypos+height-16, width, 16, 1, 0 )
		GAD_Arrow( xpos + x, ypos+height-16+6, 1, 1 )

	EndIf

End Function

Function GAD_Spinner( gt.gtype )

	; draw the textfield

	GAD_TextField( gt )

	; draw the boxes with arrows

	xpos = gt\xpos + gt\width + 2
	ypos = gt\ypos
	width = 16
	height = gt\height / 2

	x = width / 2 - 3	; xpos of little arrow
	GAD_Box( xpos, ypos, width, height, gt\enable, 0 )
	GAD_Arrow( xpos+x, ypos+(height/2)-2, gt\enable, 2 )
	GAD_Box( xpos,ypos+height, width, height, gt\enable, 0 )
	GAD_Arrow( xpos+x, ypos+height+(height/2)-2, gt\enable, 1 )

End Function

Function GAD_Imagebox( gt.gtype )

	 ; draws an image box

	; draw border

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Rect gt\xpos,gt\ypos,gt\width,gt\height, 0

	; draw the image or text

	If gt\image$ <> ""
		DrawImage gt\imagehandle, gt\xpos,gt\ypos
	Else
		Color 0,0,0
		Text gt\xpos+2, gt\ypos+2,"no image"
	EndIf

End Function

Function GAD_ColSelector( gt.gtype )

	; draws an color selector box

	; border.

	GAD_Box( gt\xpos,gt\ypos,gt\width,gt\height, True,1 )

	; draw color box

	Color gt\r,gt\g,gt\b
	Rect gt\xpos+2,gt\ypos+2,gt\width-4,gt\height-4, 1

End Function

Function GAD_Splitter( gt.gtype )

	; draws a splitter gadget

	Color editor\gad_shade_r, editor\gad_shade_g, editor\gad_shade_b
	Rect gt\xpos,gt\ypos,gt\width,gt\height, 1

End Function

Function FitStringToWidth$( txt$, width )

	; returns a string which is no longer than the passed width
	; used for clipping texts on gadgets.

	For count = 1 To Len( txt$ )
		If StringWidth( Left$( txt$, count ) ) > width
			Return Left$( txt, count - 1 )
		EndIf
	Next

	Return txt$

End Function