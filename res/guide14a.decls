.lib "user32.dll"

; decls api calls from GUIde (1.4a)

; lock window to prevent canvas flicker
;http://www.blitzbasic.co.nz/bbs/posts.php?topic=25303
api_LockWindowUpdate(hWnd%) : "LockWindowUpdate"

; get windows info
api_GetSysColor%(index%):"GetSysColor"
api_GetSystemMetrics%(index%):"GetSystemMetrics"

; mouse cursor change
api_LoadCursor%( ID, Cursor ):"LoadCursorA" 
api_SetCursor%( ID ):"SetCursor"

; mousex and y relative to every gadget
; needed by mousex and mousey functions
; http://www.blitzbasic.com/Community/posts.php?topic=26130
api_GetCursorPos% (lpPoint*) : "GetCursorPos"
api_ScreenToClient% (hwnd%, lpPoint*) : "ScreenToClient"


; skn3 gadgetints
GadgetPropertiesSetProp%(Hwnd%,Name$,Pointer%):"SetPropA"
GadgetPropertiesGetProp%(Hwnd%,Name$):"GetPropA"
GadgetPropertiesRemoveProp%(Hwnd%,Name$):"RemovePropA"

; used for various functions, mainly to select all text in a textbox
api_SendMessage%(hwnd%, wMsg%, wParam%, lParam%):"SendMessageA"
