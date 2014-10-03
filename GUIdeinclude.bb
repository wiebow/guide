
; gadget toevoegen:
; 1. const voor gadget aanmaken
; 2. editor type uitbreiden met ...count
; 3. ResetGadgetCounters()
; 4; UpdateCounter()
; 5. CreateGadget()
; 6. DrawGadget() call naar GAD_..... toevoegen
; 7. ShowGadgetDetails()
; 8. CreatePropInput()
; 9. StorePropInput()
; 10. Save functie uitbreiden met eventueel unieke velden
; 11. Export maken.

Global APP$

If DEMO
	APP$ = "GUIde " + APPVERSION$ + " - DEMO version"
Else
	APP$ = "GUIde " + APPVERSION$
EndIf

AppTitle APP$

;#Region Create main window
	
Global MAINWINDOW = CreateWindow( APP$, 220,200,320,540,0,1+2+4+8 )
SetMinWindowSize MAINWINDOW

menu = WindowMenu( MAINWINDOW )

file = CreateMenu( "File", 0, menu )
CreateMenu( "New Project	CTRL+N",FILENEW, file )
CreateMenu( "Load Project...	CTRL+L", FILEOPEN, file )
CreateMenu( "Save	CTRL+S", FILESAVE, file )
CreateMenu( "Save Project as...", FILESAVEAS, file )
sep = CreateMenu( "", 0, file )

If demo = False
	CreateMenu( "Export code...", FILEEXPORT, file )
	sep = CreateMenu( "", 0, file )
EndIf

CreateMenu( "Exit", FILEEXIT, file )

edit = CreateMenu( "&Edit", 1, menu )
CreateMenu( "Delete	CTRL-DEL", EDITDELETE, edit )
CreateMenu( "Clone	CTRL-SPACE", EDITCLONE, edit )
CreateMenu( "", 0, edit )
CreateMenu( "Select All	CTRL-A", EDITSELECTALL, edit )

view = CreateMenu( "View", 2, menu )
CreateMenu( "Options...	CTRL-O",VIEWOPTIONS, view )

help = CreateMenu( "Help", 4, menu )
CreateMenu( "About...", HELPABOUT, help )
CreateMenu( "Keys...	F1", HELPKEYS, help )

UpdateWindowMenu MAINWINDOW

; main window gadgets
Global GADGETTREE = CreateTreeView( 98,0, ClientWidth( MAINWINDOW )-100, 150, MAINWINDOW )
Global VERSIONLABEL = CreateLabel( "Type: ", 100, 153, ClientWidth( MAINWINDOW )-100, 21, MAINWINDOW )
Global GADGETPROPBOX = CreateListBox( 98,200, ClientWidth( MAINWINDOW )-100, 270, MAINWINDOW )

Global blitzplusstrip = LoadIconStrip( "res\blitzplus-strip.png" )
SetGadgetIconStrip GADGETTREE, blitzplusstrip

; color and image selection buttons
Global PANELCOL = CreateButton( "Select...",100,176, 60,21, MAINWINDOW )
Global PANELIMG = CreateButton( "Select...",100,176, 60,21, MAINWINDOW )
Global AREACOL = CreateButton( "Select...",100,176, 60,21, MAINWINDOW )
Global AREATXT = CreateButton( "Select...",100,176, 60,21, MAINWINDOW )
Global DEF = CreateButton( "Reset", 238,176, 60,21, MAINWINDOW )

; main input gadgets
Global PROPTEXT =  CreateTextField( 99,176, ClientWidth( MAINWINDOW )-3-99,21, MAINWINDOW )
Global PROPSELECT = CreateButton( "Select",99, 176,GadgetWidth( MAINWINDOW )-60-99,21, MAINWINDOW, 2 )
Global PROPCOMBO = CreateComboBox( 99, 176,GadgetWidth( MAINWINDOW )-60-99,21, MAINWINDOW, 2 )

Global MENUTXT = CreateTextField( 99,176, ClientWidth( MAINWINDOW )-3-99,21, MAINWINDOW )
Global MENUCOMBO = CreateComboBox( 99, 176,GadgetWidth( MAINWINDOW )-60-99,21, MAINWINDOW, 2 )
Global MENUCHECK = CreateButton( "",99, 176,GadgetWidth( MAINWINDOW )-60-99,21, MAINWINDOW, 2 )

SetGadgetLayout GADGETTREE, 1,1,1,1
SetGadgetLayout GADGETPROPBOX, 1,1,0,1

SetGadgetLayout VERSIONLABEL, 1,1,0,1
SetGadgetLayout PANELCOL, 1,0,0,1
SetGadgetLayout PANELIMG, 1,1,0,1
SetGadgetLayout AREACOL, 1,1,0,1
SetGadgetLayout AREATXT, 1,1,0,1
SetGadgetLayout DEF,0,1,0,1
SetGadgetLayout PROPTEXT, 1,1,0,1
SetGadgetLayout PROPSELECT, 1,1,0,1
SetGadgetLayout PROPCOMBO, 1,1,0,1
SetGadgetLayout MENUTXT, 1,1,0,1
SetGadgetLayout MENUCOMBO, 1,1,0,1
SetGadgetLayout MENUCHECK, 1,1,0,1

HideGadget PANELCOL
HideGadget PANELIMG
HideGadget AREACOL
HideGadget AREATXT
HideGadget DEF
HideGadget PROPTEXT
HideGadget PROPSELECT
HideGadget PROPCOMBO
HideGadget MENUCOMBO
HideGadget MENUTXT
HideGadget MENUCHECK

;------------------------------------------------------------------
;TOOLCANVAS

; language selector
;Global TOOLCOMBO = CreateComboBox( 0,0,96,20,MAINWINDOW)
;AddGadgetItem TOOLCOMBO,"BlitzPlus", 1
;AddGadgetItem TOOLCOMBO,"WinBlitz3D"
;SetGadgetLayout TOOLCOMBO,1,0,1,0

;bar
Global TOOLCANVAS = CreateCanvas( 0,28,96,369, MAINWINDOW )
SetGadgetLayout TOOLCANVAS,1,0,1,0
Global BLITZPLUSTOOLIMG = LoadImage( "res\blitzplus-tools-small.png" )

MaskImage BLITZPLUSTOOLIMG, 255,0,255

;#End Region

;#Region Create Actions window

;Global ACTIONWINDOW = CreateWindow( "", 50,50,200,300,MAINWINDOW, 1+16+32)
;Global ACTIONTEXT = CreateTextArea( 0,0,ClientWidth(ACTIONWINDOW), ClientHeight(ACTIONWINDOW)-25, ACTIONWINDOW)
;Global ACTIONOK = CreateButton( "OK",150,ClientHeight(ACTIONWINDOW)-22, 50,20, ACTIONWINDOW )

;#End Region

;#Region Create help window

Global HELPWINDOW
Global HELPOK
	
HELPWINDOW = CreateWindow( APP$ + " - Help", 0,0,247,250,MAINWINDOW, 1+16+32 )
HideGadget HELPWINDOW
SetGadgetShape HELPWINDOW, GadgetX( MAINWINDOW)+50, GadgetY( MAINWINDOW)+50,247,250

HELPOK = CreateButton( "I remember now", GadgetWidth( HELPWINDOW ) /2 - 43, GadgetHeight( HELPWINDOW ) - 61, 85,30, HELPWINDOW, 1 )
Local txtarea = CreateTextArea( 8,8,230,200,HELPWINDOW )

Local info$ = Chr$(9) + APP$ + " CONTROLS" +Chr$(13)+Chr$(10)+Chr$(13)+Chr$(10)
info$ = info$ + "LMB" +Chr$(9)+Chr$(9)+ "Select" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+LMB" +Chr$(9)+ "Multiple select" + Chr$(13) + Chr$(10)
info$ = info$ + "RMB+DRAG" +Chr$(9)+ "Selection box" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+A" +Chr$(9)+Chr$(9)+ "Select all" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+DEL" +Chr$(9)+ "Delete selection" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+SPACE" +Chr$(9)+ "Clone selection" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+ARROWS" +Chr$(9)+ "Move selection" + Chr$(13) + Chr$(10)
info$ = info$ + "ESC" +Chr$(9)+Chr$(9)+ "Deselect" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+ -" +Chr$(9)+Chr$(9)+ "Hide selection" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+ \" +Chr$(9)+Chr$(9)+ "Unhide gadgets" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+S" +Chr$(9)+Chr$(9)+ "Quick save" + Chr$(13) + Chr$(10)
info$ = info$ + "CTRL+TAB" +Chr$(9)+ "Add menu item" + Chr$(13) + Chr$(10)

SetGadgetText txtarea, info$

;#End Region

;#Region Create options window

Global OPTIONWIN,
	
Global OPTIONWIN=CreateWindow("GUIde options",0,0,202,390,MAINWINDOW, 1 )
	HideGadget OPTIONWIN
	SetGadgetShape OPTIONWIN, GadgetX( MAINWINDOW)+50, GadgetY( MAINWINDOW)+50,202,390

	Global grpGroupBox0=CreateGroup( "Editor",4,4,184,152,OPTIONWIN)
		Global chbSNAP=CreateButton("Snap gadgets to grid",8,16,164,20,grpGroupBox0,2)
			SetButtonState chbSNAP,1
			SetGadgetLayout chbSNAP,1,0,1,0
		Global chbCONSTRAIN=CreateButton("Constrain child gadgets",8,36,164,20,grpGroupBox0,2)
			SetButtonState chbCONSTRAIN,1
			SetGadgetLayout chbCONSTRAIN,1,0,1,0
		Global chbGRID=CreateButton("Show grid",8,56,164,20,grpGroupBox0,2)
			SetButtonState chbGRID,1
			SetGadgetLayout chbGRID,1,0,1,0
		CreateLabel("Grid size:",8,84,80,20,grpGroupBox0,0)
		Global tfdGRIDSIZE=CreateTextField(108,80,64,20,grpGroupBox0)
			SetGadgetText tfdGRIDSIZE,"4"
			SetGadgetLayout tfdGRIDSIZE,1,0,1,0
		Global chbLABELS=CreateButton("Show panel and canvas labels",8,104,164,20,grpGroupBox0,2)
			SetGadgetLayout chbLABELS,1,0,1,0
	Global grpGroupBox1=CreateGroup( "Export",4,160,184,168,OPTIONWIN)
;		CreateLabel("Code type:",8,24,76,20,grpGroupBox1,0)
		CreateLabel("File extension:",8,48,76,20,grpGroupBox1,0)
		CreateLabel("Hande prefix:",8,72,76,20,grpGroupBox1,0)
;		Global cobCODETYPE=CreateComboBox(93,20,79,20,grpGroupBox1)
;			AddGadgetItem cobCODETYPE,"BlitzPlus"
;			AddGadgetItem cobCODETYPE,"WinBlitz3D"
;			SelectGadgetItem cobCODETYPE,0
;			SetGadgetLayout cobCODETYPE,1,0,1,0
		Global cobEXTENSION=CreateComboBox(93,44,79,20,grpGroupBox1)
			AddGadgetItem cobEXTENSION,"*.bp"
			AddGadgetItem cobEXTENSION,"*.bb"
			SelectGadgetItem cobEXTENSION,0
			SetGadgetLayout cobEXTENSION,1,0,1,0
		Global tfdPREFIX=CreateTextField(93,68,79,20,grpGroupBox1)
			SetGadgetText tfdPREFIX,"Global"
			SetGadgetLayout tfdPREFIX,1,0,1,0
		Global chbINDENT=CreateButton("Indented code",8,100,164,20,grpGroupBox1,2)
			SetButtonState chbINDENT,1
			SetGadgetLayout chbINDENT,1,0,1,0
		Global chbHANDLES=CreateButton("Create label handles",8,120,164,20,grpGroupBox1,2)
			SetGadgetLayout chbHANDLES,1,0,1,0
		Global chbMAINLOOP=CreateButton("Export mainloop",8,140,164,20,grpGroupBox1,2)
			SetButtonState chbMAINLOOP,1
			SetGadgetLayout chbMAINLOOP,1,0,1,0
	Global btnDONE=CreateButton("Done",120,336,64,20,OPTIONWIN,1)
		SetGadgetLayout btnDONE,1,0,1,0

;#End Region

;#Region GUI globals

Global gtype.gtype, list.list, editor.editor, mtype.mtype, mlist.mlist
Global EDITWINDOW, DISPLAYCANVAS, MENUCANVAS
Global VALUEGADGET					; values get entered / selected in this gadget

	
;#End Region

;#Region definitions
	
Type Editor

	Field font
	Field mode
	Field grid.grid				; grid
	Field selection.list		; current gadgets selection
	Field mselection.mlist		; selected menu items
	Field copy.list				; list of gadgets saved with ctrl-c
	Field rootgt.gtype			; root gadget (editwindow)
	Field rootmt.mtype			; root menu

	Field activeprop			; selected item # in the prop window
	Field activecanvas			; mouse is over this canvas
;	Field activetool			; which tool bar is shown. (b+, winblit3d, etc)
	Field sidehandle			; holds the handle the mouse pointer is over. used by cornerhit()

	Field rmb, lmb				; flags
	Field dragmode				; move, scale, resizex, resizey
	Field bx,by					; origin of selection box
	Field dragx, dragy			; origin of gadget drag

	;options
	Field constraint			; flag
	Field drawgrid				; flag
	Field snaptogrid			; flag
	Field changed				; flag
	Field fileextension			; 0 = .bp, 1 = .bb
	Field exportloop			; flag
	Field labelhandles			; flag
	Field indentedcode			; flag
	Field showlabels			; flag
	Field prefix$				; default: Global
	Field indentlevel			; current level of indention
	Field loadedproject$		; current project

	; counters
	Field buttoncount			; counters for gadgets. every time a new gadget is created,
	Field labelcount			; this value gets incremented. gadgets recieve number as suffix
	Field textfieldcount		; when created
	Field textareacount
	Field checkboxcount
	Field listboxcount
	Field comboboxcount
	Field tabbercount
	Field canvascount
	Field panelcount
	Field radiobuttoncount
	Field slidercount
	Field progresscount
	Field htmlcount
	Field treeviewcount
	Field groupboxcount
	Field linecount
	Field menuidcount
	Field spinnercount
	Field tabpanelcount
	Field foldboxcount
	Field imagecount
	Field colorcount
	Field splittercount
	Field scene3dcount

	Field gadr,gadg,gadb						; gadget colors
	Field gad_text_r, gad_text_g, gad_text_b
	Field gad_shade_r, gad_shade_g, gad_shade_b
	Field gad_dshade_r, gad_dshade_g, gad_dshade_b
	Field gad_hilite_r, gad_hilite_g, gad_hilite_b

	Field winr,wing,winb						; window color
	Field win_text_r, win_text_g, win_text_b	; window text color
	Field sel_text_r,sel_text_g,sel_text_b		; selected text background color

End Type

Type Mtype
	Field childlist.mlist			; childlist of this menu item
	Field parent.mtype				; parent of this menu item
	Field garbage					; flag
	Field node						; tree node handle

	Field version					; ROOT menu, menu item, or seperator
	Field active					; flag
	Field checkable					; flag
	Field checked					; flag
	Field id						; id count
	Field label$					; display
	Field shortcut$					; shortcut key for menu item
	Field shortcutmod				; 0 = nothing, 1 = shift, 2 = ctrl, 3 = alt
End Type

;#End Region
