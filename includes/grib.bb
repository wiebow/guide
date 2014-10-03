;INCLUDE

; GRIB
; a grid lib for blitzplus
; wdw@home.nl / http://members.home.nl


Type grid
	Field name$					; grid name
	Field visible				; flag
	Field style					; flag. 0 = lines, 1 = dots
	Field zoom#					; current zoom in pixels
	Field size					; size of grid (in pixels)
	Field blocksize				; size of block grid (in pixels)
	Field xoffset, yoffset		; grid drawing offset
	Field gx#, gy#				; position on grid
	Field cx#, cy#				; position on canvas
	Field br, bg, bb			; backdrop color
	Field backcolor				; grey value of backdrop, default = 140
	Field blockcolor			; grey value of block grid. default = 100
	Field maincolor				; grey value of main grid, default = 120
	Field tolerance				; snap-to-grid tolerance, default = 5
	Field statusline$			; text showing grid size, zoom and position

End Type

Global grid
Global GR_ResultX#, GR_ResultY#	; global storage for transform functions

;------------------------------------------------------------------

Function GR_CreateGrid.grid( name$ = "", visible = True, style = 0 )

	; creates a grid

	g.grid = New grid
	g\name$ = name$
	g\visible = visible
	g\style = style
	g\zoom = 16
	g\size = 1
	g\blocksize = 16
	g\blockcolor = 100
	g\maincolor = 120
	g\backcolor = 140
	g\br = 140
	g\bg = 140
	g\bb = 140
	g\tolerance = 5
	Return g

End Function

Function GR_DrawGrid( g.grid, canvas, axis=True )

	; draws a grid on the passed canvas

	SetBuffer CanvasBuffer( canvas )
;	ClsColor g\backcolor, g\backcolor, g\backcolor
	ClsColor g\br, g\bg, g\bb
	Cls

	; determine style of grid, and draw lines or dots accordingly

	If g\visible
		If g\style = 0
			; draw main grid, if not too small
			If g\zoom * g\size > 3 Then GR_DrawGridLines( g, g\maincolor, g\size, canvas )

			; draw 16x16 block
			GR_DrawGridLines( g, g\blockcolor, g\blocksize, canvas )

		Else
			; draw dots
			GR_DrawGridLines( g, g\maincolor, g\size, canvas )
		EndIf

		If axis
			; draw middle axis lines
			Color 10,10,10
			Line 0, g\yoffset, GadgetWidth( canvas ), g\yoffset
			Line g\xoffset, 0, g\xoffset, GadgetHeight( canvas )

			; draw axis indicators
			Color g\maincolor -25, g\maincolor -25, g\maincolor -25
			Text 1, g\yoffset - 5, "-X"
			Text GadgetWidth( canvas) -18, g\yoffset - 5, "+X"
			Text g\xoffset - 8, 0, "-Y"
			Text g\xoffset - 8, GadgetHeight( canvas ) -12, "+Y"
		EndIf

	EndIf

	; draw grid name
	Color 255,255,255
	Text 1,1, g\name$

End Function

Function GR_DrawCursor( canvas, style=0, brightness# = 1.0 )

	; draws a cross cursor at the current mouse canvas position

	SetBuffer CanvasBuffer( canvas )

	brightness = brightness * 255
	Color brightness, brightness, brightness

	Local cx = MouseX( canvas )
	Local cy = MouseY( canvaS )

	Select style

		Case 2 							; rotate
			Oval cx-7,cy-7,14,14,0
			Line cx-8, cy-2, cx-10, cy
			Line cx-6, cy-2, cx-4, cy
			Line cx+3, cy-2, cx+5, cy
			Line cx+9, cy-2, cx+7, cy

		Case 3							; move
			Line cx, cy, cx+7, cy
			Line cx, cy, cx, cy+7

		Case 4							; scale
			Line cx-3, cy-3, cx-7,cy-7
			Line cx+3, cy-3, cx+7,cy-7
			Line cx-3, cy+3, cx-7,cy+7
			Line cx+3, cy+3, cx+7,cy+7

			Line cx-7, cy-7, cx-7, cy-3
			Line cx-7, cy-7, cx-3, cy-7
			Line cx+7, cy-7, cx+7, cy-3
			Line cx+7, cy-7, cx+3, cy-7
			Line cx-7, cy+7, cx-7, cy+3
			Line cx-7, cy+7, cx-3, cy+7
			Line cx+7, cy+7, cx+7, cy+3
			Line cx+7, cy+7, cx+3, cy+7

		Case 0, 1 ; 					; normal, select
			Plot cx,cy
			Line cx-7, cy, cx-3, cy
			Line cx+3, cy, cx+7, cy
			Line cx, cy-7, cx, cy-3
			Line cx, cy+3, cx, cy+7

			If style = 1				; multi select
				Line cx+2, cy-5, cx+6, cy-5
				Line cx+4, cy-7, cx+4, cy-3
			EndIf

	End Select

End Function


Function GR_SizeGrid( g.grid, size )

	; sets grid size

	g\size = size

End Function

Function GR_ZoomGrid( g.grid, zoom )

	; sets a grid zoom level

	g\zoom = zoom

End Function

Function GR_CenterGrid( g.grid, canvas, gx=0, gy=0 )

	; centers on passed grid location

	gx = -gx
	gy = -gy

	g\xoffset = gx * g\zoom + GadgetWidth( canvas ) / 2
	g\yoffset = gy * g\zoom + GadgetHeight( canvas ) / 2

End Function

Function GR_MoveGrid( g.grid, xamount, yamount )

	; moves a grid by passed amount

	g\xoffset = g\xoffset + xamount
	g\yoffset = g\yoffset + yamount

End Function

Function GR_GridToCanvas( g.grid, xpos, ypos )

	; transforms passed grid position to canvas position

	; determine and store canvas position in global result variable
	GR_ResultX = xpos * g\zoom + g\xoffset
	GR_ResultY = ypos * g\zoom + g\yoffset

End Function

Function GR_CanvasToGrid( g.grid, xpos, ypos )

	; transforms passed canvas position to grid position

	; determine and store grid position in global result variable
	GR_ResultX = ( xpos / g\zoom) - (g\xoffset / g\zoom)
	GR_ResultY = ( ypos / g\zoom) - (g\yoffset / g\zoom)

End Function

Function GR_PositionOnGrid( g.grid, tolerance )

	; returns true if the current grid position (float) is close enough to a true grid position (int)
	; this will allow for a little tolerance when hitting a grid position.

	; find nearest grid position
	nearx = g\gx / g\size
	neary = g\gy / g\size
	nearx = nearx * g\size
	neary = neary * g\size

	; convert that to canvas position
	GR_GridToCanvas( g, nearx, neary )

	; get difference from current mouse canvas position
	xdiff = Abs( GR_ResultX - g\cx )
	ydiff = Abs( GR_ResultY - g\cy )

	; convert canvas position to grid position
	GR_CanvasToGrid( g, GR_ResultX, GR_ResultY )

	;near enough?
	If xdiff < tolerance And ydiff < tolerance Then Return True
	Return False

End Function

Function GR_UpdateStatusLine( g.grid, window=0 )

	; generates the status text of a grid, and updates the status text of passed window (optional)

	; only show 2 decimals in grid statusline
	xnum$ = Str$( g\gx )
	ynum$ = Str$( g\gy )

	For count = 1 To Len( xnum$ )
		If Mid$( xnum$, count,1 ) = "."
			xnum$ = Left$( xnum$, count + 2 )
			Exit
		EndIf
	Next

	For count = 1 To Len( ynum$ )
		If Mid$( ynum$, count,1 ) = "."
			ynum$ = Left$( ynum$, count + 2 )
			Exit
		EndIf
	Next

	g\statusline$ = "Grid size: " + g\size + ". Zoom level: " + Int( g\zoom ) + ". Grid position: " + xnum$ + ", " + ynum$
	If window Then SetStatusText window, g\statusline

End Function

Function GR_DrawGridLines( g.grid, brightness, stepsize, canvas )

	; helper function, called by GR_DrawGrid.
	; draws the grid lines, or dots
	; starts on grid center and goes to 4 edges of canvas

	Color brightness, brightness, brightness

	Select g\style
		Case 0

			;vertical lines
			x = g\xoffset
			While x <= GadgetWidth( canvas )
				Line x, 0, x, GadgetHeight( canvas )
				x = x + g\zoom * stepsize
			Wend

			x = g\xoffset
			While x >= 0
				Line x, 0, x, GadgetHeight( canvas )
				x = x - g\zoom	* stepsize
			Wend

			; horizontal lines
			y = g\yoffset
			While y <= GadgetHeight( canvas )
				Line 0, y, GadgetWidth( canvas ), y
				y = y + g\zoom * stepsize
			Wend

			y = g\yoffset
			While y >= 0
				Line 0, y, GadgetWidth( canvas ), y
				y = y - g\zoom * stepsize
			Wend

		Case 1

			; top right dots
			x = g\xoffset
			While x <= GadgetWidth( canvas )
				y = g\yoffset
				While y >=0
					Plot x,y
					y = y - g\zoom * stepsize
				Wend
				x = x + g\zoom * stepsize
			Wend

			; top left dots
			x = g\xoffset
			While x >= 0
				y = g\yoffset
				While y >=0
					Plot x,y
					y = y - g\zoom * stepsize
				Wend
				x = x - g\zoom * stepsize
			Wend

			; bottom right dots
			x = g\xoffset
			While x <= GadgetWidth( canvas )
				y = g\yoffset
				While y <= GadgetHeight( canvas )
					Plot x,y
					y = y + g\zoom * stepsize
				Wend
				x = x + g\zoom * stepsize
			Wend

			; bottom rightleft dots
			x = g\xoffset
			While x >= 0
				y = g\yoffset
				While y <= GadgetHeight( canvas )
					Plot x,y
					y = y + g\zoom * stepsize
				Wend
				x = x - g\zoom * stepsize
			Wend

	End Select

End Function