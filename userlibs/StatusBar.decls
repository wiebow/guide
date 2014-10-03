.lib "user32.dll"

; http://www.blitzbasic.co.nz/codearcs/codearcs.php?code=706

SendMessageSTRING%(hwnd%, msg%, wParam%, mParam$):"SendMessageA"
SendMessageBANK%(hwnd%, msg%, wParam%, mParam*):"SendMessageA"

FindWindowEx%(hParent,hChild,classname$,title$):"FindWindowExA"
GetActiveWindow%():"GetActiveWindow"