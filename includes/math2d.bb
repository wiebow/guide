; math functions 2d
; =================

;Type point
;	Field x#, y#
;End Type

;Global point
;result.point = New point

Function Distance2D#( x1#, y1#, x2#, y2# )

	; returns the distance between 2 points

	xd# = x1 - x2
	yd# = y1 - y2
	rad# = Sqr( xd * xd + yd * yd )

	Return rad

End Function


Function PointInCircle( CircleX#, CircleY#, CircleRad#, PointX#, PointY# )

	; returns true if given point is within circle radius

	dist# = Distance2D( circleX, circleY, pointX, pointY )

	If dist < circleRad
		Return True
	Else
		Return False
	EndIf

End Function


Function PointInRect( PointX#, PointY#, RectX#, RectY#, RectW#, RectH# )

	; returns true if point is within given rectangle

	If PointX >= RectX And PointX <= RectX + RectW
		If PointY >= RectY And PointY <= RectY + RectH Then Return True
	EndIf

	Return False

End Function


Function PointLinesInterSect( x1#,y1#, x2#,y2 )

	; checking needed!?!?!?!?

	; returns the x and y position of 2 intersecting lines

	x# = x1 + ( x2 - x1 )
	y# = y1 + ( y2 - y1 )

	; put result in 2d point type

;	result\x = x
;	result\y = y

End Function


; point on line (x)
;A single linear line has the equation:
;X = A*Y
;where (in Blitz) A = (x2-x1)/(y2-y1)
;So if you know the x1,y1,x2,y2 coordinates used in a Line command, you can calculate A and stick your known Y into the first equation to find out the remaining X.




;Interpolation!
;
;A# = The distance between point 1 and 2
;B# = The distance between point 1 and the midpoint
;
;C# = B# / A#
;
;C# is now a value between 0 and 1 which indicates the relative location between the points.
;
;Now all you have to do is this:
;
;U# = U1#*(1.0-C#) + U2#*C#
;V# = V1#*(1.0-C#) + V2#*C#

