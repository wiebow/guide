;
; GUIDE 1.x constants
;

;#Region  App

;Const DEMO = True
Const DEMO = False
Const APPVERSION$ = "1.4"
	
;#End Region

;#Region GUI

Const FILESAVEAS	= 1			; menu items
Const FILESAVE		= 2
Const FILEOPEN		= 3
Const FILENEW		= 4
Const FILEEXIT		= 5
Const HELPABOUT		= 6
Const DESIGNTEST	= 7	; ?
Const VIEWGRID		= 8
Const VIEWPROPS		= 9
Const EDITSELECTALL = 10
Const EDITDELETE	= 11
Const EDITCLONE		= 12
Const EDITPASTE		= 13
Const EDITCOPY		= 14
Const EDITCUT		= 15
Const FILEEXPORT	= 16
Const HELPKEYS		= 17
Const VIEWOPTIONS	= 18

;#End Region

;#Region Gadgets
	
Const BUTTON 		= 0			; gadget types
Const LABEL 		= 1
Const TEXTFIELD 	= 2
Const TEXTAREA 		= 3
Const CHECKBOX 		= 4
Const RADIOBUTTON	= 5
Const LISTBOX 		= 6
Const COMBOBOX 		= 7
Const TREEVIEW		= 8
Const TABBER		= 9
Const PROGRESSBAR	= 10
Const CANVAS 		= 11
Const PANEL			= 12
Const SLIDER		= 13
Const HTML			= 14
Const GROUPBOX		= 15		; custom
Const SPINNER		= 16
Const SEPLINE		= 17
Const FOLDBOX1		= 18
Const IMAGE			= 19
Const COLSELECTOR	= 20
Const SPLITTER		= 21
;Const SCENE3D		= 22

Const TABPANEL		= 23		; gadget strip icons...
Const WINDOW		= 24		; if we add more normal gadgets, move these up and
Const MENUROOT		= 25		; expand the gadget treeview icon strip

Const MENUITEM		= 98

;#End Region

;#Region Editor
	
Const MODE_NORMAL		= 0		; editor modes
Const MODE_MOVE			= 1
Const MODE_BOX			= 2
Const MODE_MULTISELECT	= 3
Const MODE_MOVETL		= 4		; move topleft of gadget
Const MODE_MOVETR		= 5		; etc...
Const MODE_MOVEBL		= 6
Const MODE_MOVEBR		= 7
Const MODE_MOVELEFT		= 8
Const MODE_MOVERIGHT	= 9
Const MODE_MOVETOP		= 10
Const MODE_MOVEBOTTOM	= 11
Const MODE_SHORTCUT 	= 12

;#End Region

Const LL_SELECTION	= 0			; linked lists
Const LL_CHILD		= 1
