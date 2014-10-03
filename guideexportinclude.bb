; GUIde export include file for guide 1.4a
; If you are using custom gadgets, this file will be included in the GUIde export code.
; version: 8-1-2006

; REQUIRED
; the following lines in user32 decls file
;	 api_GetSysColor%(index%):"GetSysColor"
;	 api_LoadCursor%( ID, Cursor ):"LoadCursorA"
;	 api_SetCursor%( ID ):"SetCursor"
;	 api_GetCursorPos% (lpPoint*) : "GetCursorPos"
;	 api_ScreenToClient% (hwnd%, lpPoint*) : "ScreenToClient"
;	 GadgetPropertiesSetProp%(Hwnd%,Name$,Pointer%):"SetPropA"
;	 GadgetPropertiesGetProp%(Hwnd%,Name$):"GetPropA"
;	 GadgetPropertiesRemoveProp%(Hwnd%,Name$):"RemovePropA"
;	 api_SendMessage%(hwnd%, wMsg%, wParam%, lParam%):"SendMessageA"

; Note: a copy of Guide.decls is included in the GUIde\res folder.
; copy this file to your blitzplus/userlibs folder

SetUpGUIde()	; force set up

; -- types ----------------------------------------------------

; GUIde type holds all settings for GUIde related gadgets and actions

Type GUIde
	Field font											; font used on custom gadgets
	Field gadr,gadg,gadb								; gadget color
	Field gad_text_r, gad_text_g, gad_text_b			; gadget text color
	Field gad_shade_r, gad_shade_g, gad_shade_b			; light shade
	Field gad_dshade_r, gad_dshade_g, gad_dshade_b		; dark shade
	Field gad_hilite_r, gad_hilite_g, gad_hilite_b		; hilite
	Field winr,wing,winb								; window color
	Field win_text_r, win_text_g, win_text_b			; window text color
	Field sel_text_r,sel_text_g,sel_text_b				; selected text background color

	Field foldboxmove									; true or false
	Field foldboxypos									; old position of ypos
	Field movepanel										; panel holding moving foldboxes

End Type

;#Region Custom Gadgets

Type FoldBox
	Field status					; open or folded
	Field opencanvas				; pointer to open box
	Field foldedcanvas				; pointer to folded box
	Field parentpanel				; pointer to parent panel
	Field prev.foldbox				; linked list
	Field nxt.foldbox
End Type

Type Spinner
	Field textgad					; pointer to textbox gadget
	Field slider					; pointer to arrows gadget
	Field integer					; flag
	Field stp#						; step value
	Field minval#
	Field maxval#
	Field prefix$
	Field suffix$					; data suffix
End Type

Type CSelector
	Field r%,g%,b%
	Field canvas
End Type

Type Splitter
	Field orientation%			; 0 = horizontal, 1 = vertical
	Field lcount%, rcount%		; left,right,top,bottom gaddget counters
	Field tcount%, bcount%
	Field canvas				; display
End Type

;#End Region

; -- constants -----------------------------------------------

Const COLOR_3DDKSHADOW=21
Const COLOR_3DFACE=15
Const COLOR_3DHILIGHT=20
Const COLOR_3DSHADOW=16
Const COLOR_BTNTEXT=18
Const COLOR_HIGHLIGHT=13
Const COLOR_WINDOW=5
Const COLOR_WINDOWTEXT=8

; -- globals -------------------------------------------------

Global GUIde.GUIde, FoldBox

; -- functions -----------------------------------------------

Function SetUpGUIde()

	GUIde.GUIde = New GUIde

	GUIde\font = LoadFont("Tahoma",13 )			; alter this if you want to change the font
												; on custom gadgets and labels
	; get system colors
	col=api_GetSysColor(COLOR_3DFACE)
	GUIde\gadr=col And $ff
	GUIde\gadg=col Shr 8 And $ff
	GUIde\gadb=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_BTNTEXT)
	GUIde\gad_text_r=col And $ff
	GUIde\gad_text_g=col Shr 8 And $ff
	GUIde\gad_text_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_3DHILIGHT)
	GUIde\gad_hilite_r=col And $ff
	GUIde\gad_hilite_g=col Shr 8 And $ff
	GUIde\gad_hilite_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_3DSHADOW)
	GUIde\gad_shade_r=col And $ff
	GUIde\gad_shade_g=col Shr 8 And $ff
	GUIde\gad_shade_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_3DDKSHADOW)
	GUIde\gad_dshade_r=col And $ff
	GUIde\gad_dshade_g=col Shr 8 And $ff
	GUIde\gad_dshade_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_WINDOW)
	GUIde\winr=col And $ff
	GUIde\wing=col Shr 8 And $ff
	GUIde\winb=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_WINDOWTEXT)
	GUIde\win_text_r=col And $ff
	GUIde\win_text_g=col Shr 8 And $ff
	GUIde\win_text_b=col Shr 16 And $ff

	col=api_GetSysColor(COLOR_HIGHLIGHT)
	GUIde\sel_text_r=col And $ff
	GUIde\sel_text_g=col Shr 8 And $ff
	GUIde\sel_text_b=col Shr 16 And $ff

End Function

Function CreateGroup( label$, xpos,ypos, width,height, group, collapsable = False, active=1 )

	; creates a labeled group gadget

	cnv = CreateCanvas( xpos,ypos,width,height,group )
	SetBuffer CanvasBuffer( cnv )
	SetFont GUIde\font

	ClsColor guide\gadr,guide\gadg,guide\gadb
	Cls

	Color guide\gad_hilite_r, guide\gad_hilite_g, guide\gad_hilite_b
	Rect 0,6,width,height-6, 0
	Rect 1,7,width-2,height-8, 0
	Color guide\gad_shade_r, guide\gad_shade_g, guide\gad_shade_b
	Rect 0,6,width-1,height-7, 0

	Color guide\gadr,guide\gadg,guide\gadb
	Rect 4, 0, StringWidth( label$ ) + 8, StringHeight( label$ ), 1
	Color guide\win_text_r, guide\win_text_g, guide\win_text_b
	Text 8, 0, label$

	FlipCanvas cnv
	Return cnv

End Function

Function CreateLine( label$, xpos,ypos, width,height, group, style=0 )

	; creates a divider line gadget

	cnv = CreateCanvas( xpos,ypos,width,height,group )
	SetBuffer CanvasBuffer( cnv )

	ClsColor guide\gadr,guide\gadg,guide\gadb
	Cls

	Select style
		Case 0						; horizontal
			ypos = GadgetHeight( cnv ) / 2
			Color guide\gad_shade_r, guide\gad_shade_g, guide\gad_shade_b
			Line 0,ypos, width, ypos
			Color guide\gad_hilite_r, guide\gad_hilite_g, guide\gad_hilite_b
			Line 0,ypos+1, width, ypos+1

			If label$ <> ""			; then draw label
				SetFont GUIde\font
				Color guide\gadr,guide\gadg,guide\gadb
				Rect 4, ypos - StringHeight( label$) / 2, StringWidth( label$ )+8, StringHeight( label$ ), 1
				Color guide\win_text_r, guide\win_text_g, guide\win_text_b
				Text 8, ypos - StringHeight( label$) / 2, label$
			EndIf

		Case 1						; vertical. no label
			xpos = width / 2
			Color guide\gad_shade_r, guide\gad_shade_g, guide\gad_shade_b
			Line xpos, 0, xpos, height
			Color guide\gad_hilite_r, guide\gad_hilite_g, guide\gad_hilite_b
			Line xpos+1, 0, xpos+1, height

	End Select

	FlipCanvas cnv
	Return cnv

End Function

Function CreateFoldBox.FoldBox( label$, ypos, height, group )

	; creates a foldeable groupbox, as the last child in the same linked list.

	Local width = ClientWidth( group )

	; create a new foldbox
	b.foldbox = New foldbox

	; find a foldbox on the same parent
	For b1.foldbox = Each foldbox
		If b1\parentpanel = group

			; now find the last in this list
			While b1\nxt <> Null
				b1 = b1\nxt
			Wend
					
			; add to list
			b1\nxt = b
			b\prev = b1
			Exit
		EndIf
	Next

	b\status = 1	; open
	b\parentpanel = group

	b\opencanvas = CreateCanvas( 0,ypos, width,height, group )
	b\foldedcanvas = CreateCanvas( 0,ypos, width,16, group )
	SetGadgetLayout b\opencanvas, 0,0,1,0
	SetGadgetLayout b\foldedcanvas, 0,0,1,0
	SetBuffer CanvasBuffer( b\foldedcanvas )
	SetFont GUIde\font
	ClsColor guide\gadr,guide\gadg,guide\gadb
	Cls

	; first, draw the folded groupbox

	; border
	Color guide\gad_hilite_r, guide\gad_hilite_g, guide\gad_hilite_b
	Rect 1,6, width-2,16-5, 0
	Rect 2,7, width-3,16-7, 0
	Color guide\gad_shade_r, guide\gad_shade_g, guide\gad_shade_b
	Rect 1,6, width-3,16-6, 0

	; title box
	Color guide\gadr-30,guide\gadg-30,guide\gadb-30
	Rect 6,1, width-12, StringHeight( label$ ), 1
	Color guide\gad_shade_r, guide\gad_shade_g, guide\gad_shade_b
	Rect 6,1, width-12, StringHeight( label$ ), 0
	Color guide\win_text_r, guide\win_text_g, guide\win_text_b
	Text 10,1, "+ " + label$
	FlipCanvas b\foldedcanvas
	HideGadget b\foldedcanvas

	; then, draw the open groupbox
	SetBuffer CanvasBuffer( b\opencanvas )
	SetFont GUIde\font
	ClsColor guide\gadr,guide\gadg,guide\gadb
	Cls

	Color guide\gad_hilite_r, guide\gad_hilite_g, guide\gad_hilite_b
	Rect 1,6, width-2,height-7, 0
	Rect 2,7, width-3,height-9, 0
	Color guide\gad_shade_r, guide\gad_shade_g, guide\gad_shade_b
	Rect 1,6, width-3,height-8, 0

	Color guide\gadr-30,guide\gadg-30,guide\gadb-30
	Rect 6,1, width-12, StringHeight( label$ ), 1
	Color guide\gad_shade_r, guide\gad_shade_g, guide\gad_shade_b
	Rect 6,1, width-12, StringHeight( label$ ), 0

	Color guide\win_text_r, guide\win_text_g, guide\win_text_b
	Text 10,1, "- " + label$
	FlipCanvas b\opencanvas

	; return handle. child gadgets should be added after returning from this function
	Return b
End Function


Function FlipFoldBox( b.foldbox )

	; flips the status of a foldable groupbox
	; and moves other boxes in the same list up or down

	If b\status = 1
		HideGadget( b\opencanvas )
		ShowGadget( b\foldedcanvas )
		b\status = 0
	Else
		ShowGadget( b\opencanvas )
		HideGadget( b\foldedcanvas )
		b\status = 1
	EndIf

	; determine open/close distance

	Local distance = GadgetHeight( b\opencanvas ) - 16

	; determine direction of movement

	If b\status = 0 Then distance = -distance

	; loop through next groupboxes, and move them

	b = b\nxt

	While b <> Null
		SetGadgetShape b\opencanvas, 0, GadgetY(b\opencanvas) + distance, GadgetWidth(b\opencanvas), GadgetHeight(b\opencanvas)
		SetGadgetShape b\foldedcanvas, 0, GadgetY(b\foldedcanvas) + distance, GadgetWidth(b\foldedcanvas), GadgetHeight(b\foldedcanvas)
		b = b\nxt
	Wend

End Function


Function ScrollFoldBoxes()

	; moves all foldboxes by distance travelled. used for scrolling the boxes
	; get old mouse ypos, store this ypos

	pan = GUIde\movepanel			; we want to move foldboxes on this panel

	oldy = GUIde\foldboxypos
	GUIde\foldboxypos = MouseY( pan )
	distance = oldy - GUIde\foldboxypos		; get mouse distance moved
	
	;
	;find a foldbox on the panel
	
	For b.foldbox = Each foldbox
		If b\parentpanel = pan
			;
			;find the first foldbox on the panel
			While b\prev <> Null
				b = b\prev
			Wend
			Exit
		EndIf
	Next
			
	; top of first box can move outside panel, but not lower than top of panel when moving down.

	If distance < 0		; moving groupboxes down

		Local newypos = GadgetY( b\opencanvas ) - distance	; determine new ypos of top groupbox
		If newypos > 2 Then distance = distance + newypos - 2	; change distance to keep title at top of panel

	ElseIf distance > 0	; moving groupboxes up

		; bottom of last foldbox can move outside panel, but should not leave bottom of panel when
		; moving up

		While b\nxt <> Null			; find the last foldbox
			b = b\nxt
		Wend

		If b\status = 1				; use open or folded canvas?
			cnv = b\opencanvas
		Else
			cnv = b\foldedcanvas
		EndIf

		; only check up movement if bottom of foldbox is outside panel

		If GadgetY( cnv ) + GadgetHeight( cnv ) > GadgetHeight( pan ) - 6
			newbottomypos = GadgetY( cnv ) + GadgetHeight( cnv ) - distance
		Else
			Return			; don't move if bottom is inside panel
		EndIf

		If newbottomypos <= GadgetHeight( pan ) - 6
			difference = newbottomypos - GadgetHeight( pan )
			distance = distance + difference + 6
		EndIf
	EndIf
	
	; move the foldboxes in this list.

	While b <> Null	
		SetGadgetShape b\opencanvas, 0, GadgetY(b\opencanvas) - distance, GadgetWidth(b\opencanvas), GadgetHeight(b\opencanvas)
		SetGadgetShape b\foldedcanvas, 0, GadgetY(b\foldedcanvas) - distance, GadgetWidth(b\foldedcanvas), GadgetHeight(b\foldedcanvas)
		b = b\nxt
	Wend

End Function

Function CheckFoldBoxes(event, button)


	; user has clicked mouse button on a canvas.
	
	; if the mouse is over a panel containing foldbox canvasses
	; then action is taken according to mouse button pressed
	
	Select button
		Case 1	
			For b.foldbox = Each foldbox
				If event = b\opencanvas And MouseY( b\opencanvas ) < 14
					FlipFoldBox(b)		; close the groupbox
					Return
		
				ElseIf event = b\foldedcanvas And MouseY( b\foldedcanvas ) < 14
					FlipFoldBox(b)		; open the groupbox
					Return
				EndIf
			Next
		Case 2
			For b.foldbox = Each foldbox
			
				; get parent panel of this foldbox

				pan = b\parentpanel

				If MouseX( pan ) >=0 And MouseX( pan ) <= GadgetWidth( pan )
					If MouseY( pan ) >=0 And MouseY( pan ) <= GadgetHeight( pan )

						; mouse is over this panel. set move flag and store current ypos on panel

						GUIde\foldboxmove = True
						GUIde\movepanel = pan				; store pointer to this panel. used in ScrollFoldBoxes()
						GUIde\foldboxypos = MouseY(pan)
						api_SetCursor(api_LoadCursor(0, 32652))		; create arrow cursor
						Return
					EndIf
				EndIf
			Next		
		
	End Select
			

End Function


Function CreateSpinner.spinner( xpos,ypos, width, height, group, integer, value#, minval#, maxval#, stepval#, prefix$ ="", suffix$ = "" )

	; creates a spinner gadget

	s.spinner = New spinner

		s\integer = integer						; 0 = float, 1 = int
		s\minval = minval
		s\maxval = maxval
		s\stp = stepval
		s\suffix$ = suffix
		s\prefix$ = prefix

		s\textgad = CreateTextField( xpos,ypos,width,height, group )
		s\slider = CreateSlider( xpos+width+1,ypos,16,height, group, 2 )

		; set slider range and value
		; slider value is set in the middle of the slider range.
		; so slidervalue 0 is up and slidervalue 2 is down. 1 is default value

		SetSliderRange s\slider, 1, 3
		SetSliderValue s\slider, 1

		; put value in spinner. show float or int according to spinner type
		; internally, we keep the float settings to simplify the CheckSpinners() function.

		If s\integer = True
			val = Int(value)
		Else
			val = value
		EndIf

		SetGadgetText s\textgad, val + s\suffix$

	Return s

End Function

Function CheckSpinners.spinner( event )

	; checks if we've hit a spinner gadget, and updates it accordingly
	; also returns the handle of the changed spinner, or Null

	For s.spinner = Each spinner

		If s\slider = event

			; we've hit this spinner slider gadget, adjust value accordingly

			; determine new value

			value# = TextFieldText( s\textgad )

			If SliderValue( s\slider ) = 2		; down
				value = value - s\stp
				If value < s\minval Then value = s\minval
			Else								; up
				value = value + s\stp
				If value > s\maxval Then value = s\maxval
			EndIf

			; transform

			If s\integer = True
				val = Int(value)
			Else
				val = value
			EndIf

			SetGadgetText s\textgad, val + s\suffix$

			; set slider value back to the middle of the range

			SetSliderValue s\slider, 1

			Return s

		EndIf
	Next

	Return Null

End Function

Function EnableSpinner( s.spinner )

	EnableGadget s\textgad
	EnableGadget s\slider

End Function

Function DisableSpinner( s.spinner )

	DisableGadget s\textgad
	DisableGadget s\slider

End Function

Function SpinnerValue#( s.spinner )

	Return GadgetText( s\textgad )

End Function

Function CreateColorSelector.cselector( xpos, ypos, width,height, r%,g%,b%, group )

	; creates a new color selector

	c.cselector = New cselector
	c\canvas = CreateCanvas( xpos,ypos, width, height, group )

	SetColorSelector( c, r,g,b )

	Return c

End Function

Function SetColorSelector( c.cselector, red%,green%,blue% )

	; updates the canvas color of the color selector

	Local width% = GadgetWidth(c\canvas)
	Local height% = GadgetHeight(c\canvas)

	; set color

	c\r = red
	c\g = green
	c\b = blue

	SetBuffer CanvasBuffer(c\canvas)
	ClsColor c\r,c\g,c\b
	Cls

	; draw border

	Color GUIde\gad_dshade_r, GUIde\gad_dshade_g, GUIde\gad_dshade_b
	Line 0,0, width, 0
	Line 0,0, 0,height

	Color GUIde\gad_hilite_r, GUIde\gad_hilite_g, GUIde\gad_hilite_b
	Line width,0, width, height
	Line 0,height, width,height

	FlipCanvas(c\canvas)

End Function

Function CheckColorSelectors( canvas )

	; checks if user has hit a color selector and asks for new color

	For c.CSelector = Each CSelector
		If c\canvas = canvas
			RequestColor( c\r,c\g,c\b )
			c\r = RequestedRed()
			c\g = RequestedGreen()
			c\b = RequestedBlue()
			SetColorSelector(c, c\r,c\g,c\b)
			Return
		EndIf
	Next

End Function

Function ColorSelectorRed( c.cselector )

	; returns the red component of passed color selector type

	Return c\r

End Function

Function ColorSelectorGreen( c.cselector )

	; returns the green component of passed color selector type

	Return c\g

End Function

Function ColorSelectorBlue( c.cselector )

	; returns the blue component of passed color selector type

	Return c\b

End Function

Function CreateImageBox( xpos, ypos, image$, group)

	; creates an image box
	; the box is scaled to the image loaded

	Local img% = LoadImage( image$ )

	If img
		Local width% = ImageWidth( img )
		Local height% = ImageHeight( img )

		imgpanel = CreatePanel( xpos,ypos, width,height, group )
		SetPanelImage imgpanel, image$

		FreeImage img
	Else
		RuntimeError "Could not load image: " + image$
	EndIf

	Return imgpanel

End Function

Function CreateSplitter.splitter( xpos%,ypos%, width%, height%, orientation%, group )

	; creates a splitter
	; 0 = horizontal, 1 = vertical

	s.splitter = New splitter
	s\orientation = orientation

	s\canvas = CreateCanvas( xpos,ypos,width,height, group )

	Select orientation
		Case 0
			SetGadgetLayout s\canvas,1,1,1,0
		Case 1
			SetGadgetLayout s\canvas,1,0,1,1
	End Select

	; set splitter canvas color to system gadget face color

	col=api_GetSysColor(15)
	SetBuffer CanvasBuffer( s\canvas )
	ClsColor col And $ff,col Shr 8 And $ff,col Shr 16 And $ff
	Cls
	FlipCanvas s\canvas

	Return s

End Function

Function FreeSplitter( s.splitter )

	; deletes a splitter and the links to gadgets

	For count = 1 To s\lcount
		FreeGadgetInt(s\canvas,"left"+count)
	Next

	For count = 1 To s\rcount
		FreeGadgetInt(s\canvas,"right"+count)
	Next

	For count = 1 To s\tcount
		FreeGadgetInt(s\canvas,"top"+count)
	Next

	For count = 1 To s\bcount
		FreeGadgetInt(s\canvas,"bottom"+count)
	Next

	FreeGadget s\canvas
	Delete s

End Function

Function LinkToSplitter( gadget%, s.splitter, side% )

	; links a gadget to a splitter
	; side can be 0 to 3
	; the opposide side of the linked gadget stays in place when the splitter is moved.

	Select side
		Case 0
			s\lcount = s\lcount + 1
			CreateGadgetInt( s\canvas,"left" + s\lcount , gadget )
		Case 1
			s\rcount = s\rcount + 1
			CreateGadgetInt( s\canvas,"right" + s\rcount , gadget )
		Case 2
			s\tcount = s\tcount + 1
			CreateGadgetInt( s\canvas,"top" + s\tcount , gadget )
		Case 3
			s\bcount = s\bcount + 1
			CreateGadgetInt( s\canvas,"bottom" + s\bcount , gadget )
	End Select

End Function

Function UpdateSplitter( s.splitter )

	; updates a splitter and its linked gadgets

	Local xmov%, ymov%, gad%, width%, height%

	Select s\orientation
		Case 0					; horizontal splitter

			api_SetCursor(api_LoadCursor(0, 32645))
			If MouseDown(1) = False Then Return

			; get move amount
			ymov%= GadgetY(s\canvas)+EventY()

			; do checks. stop movement if splitter is forcing linked gadgets to 'overlap'
			; top side

			For count = 1 To s\tcount
				gad% = GetGadgetInt(s\canvas,"top" + count )
				If ymov <= GadgetY(gad) + 4
					ymov = GadgetY(gad) + 4
					Exit
				EndIf
			Next

			; bottom side

			For count = 1 To s\bcount
				gad% = GetGadgetInt(s\canvas,"bottom" + count )
				If ymov + GadgetHeight(s\canvas)+4 >= GadgetY(gad)+GadgetHeight(gad)
					ymov = GadgetY(gad)+GadgetHeight(gad)-GadgetHeight(s\canvas)-4
					Exit
				EndIf
			Next

			; continue. move splitter
			SetGadgetShape s\canvas, GadgetX(s\canvas),ymov, GadgetWidth(s\canvas), GadgetHeight( s\canvas )

			; resize top side gadgets

			For count = 1 To s\tcount
				gad% = GetGadgetInt(s\canvas,"top" + count )
				SetGadgetShape gad, GadgetX(gad), GadgetY(gad), GadgetWidth(gad), (GadgetY(s\canvas) - GadgetY(gad))
			Next

			; resize bottom side gadgets
			; the bottom of the bottomside gadgets stay where they are.

			For count = 1 To s\bcount
				gad% = GetGadgetInt(s\canvas,"bottom" + count )
				height% = (GadgetY(gad)+GadgetHeight(gad)) - (GadgetY(s\canvas)+GadgetHeight( s\canvas ))
				SetGadgetShape gad, GadgetX(gad),GadgetY(s\canvas)+GadgetHeight( s\canvas ), GadgetWidth(gad), height
			Next

		Case 1					; vertical splitter

			api_SetCursor(api_LoadCursor(0, 32644))
			If MouseDown(1) = False Then Return

			; get move amount
			xmov%= GadgetX(s\canvas)+EventX()

			; do checks. stop movement if splitter is forcing linked gadgets to 'overlap'
			; left side

			For count = 1 To s\lcount
				gad% = GetGadgetInt(s\canvas,"left" + count )
				If xmov - GadgetX(gad) <= 4
					xmov = GadgetX(gad) + 4
					Exit
				EndIf
			Next

			; right side

			For count = 1 To s\rcount
				gad% = GetGadgetInt(s\canvas,"right" + count )
				If (GadgetX(gad)+GadgetWidth(gad)) - ( xmov+GadgetWidth(s\canvas) ) < GadgetWidth(s\canvas) - 4
					xmov = GadgetX(gad)+GadgetWidth(gad) - GadgetWidth(s\canvas) - 4
					Exit
				EndIf
			Next

			; continue. move splitter

			SetGadgetShape s\canvas, xmov,GadgetY( s\canvas ), GadgetWidth(s\canvas), GadgetHeight( s\canvas )

			; move linked gadgets
			; left side

			For count = 1 To s\lcount
				gad% = GetGadgetInt(s\canvas,"left" + count )
				SetGadgetShape gad, GadgetX(gad), GadgetY(gad), GadgetX(s\canvas)-GadgetX(gad), GadgetHeight(gad)
			Next

			; right side

			For count = 1 To s\rcount
				gad% = GetGadgetInt(s\canvas,"right" + count )

				; determine new width value
				width% = (GadgetX(gad)+GadgetWidth(gad)) - ( GadgetX(s\canvas)+GadgetWidth(s\canvas) )

				; set new shape
				SetGadgetShape gad, GadgetX(s\canvas)+GadgetWidth(s\canvas), GadgetY(gad), width, GadgetHeight(gad)
			Next

	End Select

End Function

; gadget int functions by Skn3

Function CreateGadgetInt(gadget,name$,value=0)
	GadgetPropertiesSetProp(QueryObject(gadget,1),name$,value)
End Function

Function GetGadgetInt(gadget,name$)
	Return GadgetPropertiesGetProp(QueryObject(gadget,1),name$)
End Function

Function SetGadgetInt(gadget,name$,value)
	GadgetPropertiesSetProp(QueryObject(gadget,1),name$,value)
End Function

Function FreeGadgetInt(gadget,name$)
	Return GadgetPropertiesRemoveProp(QueryObject(gadget,1),name$)
End Function

Function MouseX( gadget=0 )

	; this mousex function will get the position of the mouse relative to any gadget. by Halo

	If Not gadget gadget=Desktop()
	hwnd=QueryObject(gadget,1)
	buffer=CreateBank (8)
	api_GetCursorPos (buffer)
	api_ScreenToClient (hwnd, buffer)
	x=PeekInt(buffer,0)
	FreeBank buffer

	Return x

End Function

Function MouseY( gadget=0 )

	; this mousey function will get the position of the mouse relative to any gadget. thanks Halo

	If Not gadget gadget=Desktop()
	hwnd=QueryObject(gadget,1)
	buffer=CreateBank(8)
	api_GetCursorPos(buffer)
	api_ScreenToClient(hwnd, buffer)
	y=PeekInt(buffer,4)
	FreeBank buffer

	Return y

End Function