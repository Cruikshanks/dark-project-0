Global mainFont
Global largeFont
Global smallFont

Global alienDeathSnd
Global playerDeathSnd
Global selectSnd
Global menuSnd
Global playerShootSnd
Global collisionSnd
Global levelStartSnd
Global levelEndSnd

;Used in general to stop counters going beyond a maximum limit
Function Max(variable,maxLimit)
	
	If variable > maxLimit Then 
		Return (maxLimit)
	Else 
		Return variable
	EndIf
	
End Function

;Used in general to stop counters going beyond a minimum limit
Function Min(variable,minLimit)
	
	If variable < minLimit Then 
		Return (minLimit)
	Else
		Return variable
	EndIf
	
End Function

;Check an image loaded OK
Function CheckImage(image, name$)

	If image = 0 Then
		error1$ = name$ + " did not load."
		error2$ = "Please ensure you have not moved/modfied the 'graphics' directory"
		Error(error1, error2)
	EndIf

End Function

Function CheckSound(sound, name$)

	If Not sound Then
		error1$ = name$ + " did not load."
		error2$ = "Please ensure you have not moved/modfied the 'sounds' directory"
		Error(error1, error2)
	EndIf

End Function

;Error Function
Function Error(error1$,error2$)

	FlushKeys
	Janitor()
	RuntimeError error1$ + " " + error2$
	
End Function

;Loads and sets the games main font
Function LoadFonts()

	mainFont = LoadFont("Arial", 18, True, False, False)
	largeFont = LoadFont("Arial", 24, True, False, False)
	smallFont = LoadFont("Arial", 14, True, False, False)

End Function

Function LoadSounds()

	alienDeathSnd = LoadSound("Sounds\aliendeath.wav")
	CheckSound(alienDeathSnd, "Sounds\aliendeath.wav")
	
	playerDeathSnd = LoadSound("Sounds\playerdeath.wav")
	CheckSound(playerDeathSnd, "Sounds\playerdeath.wav")
	
	selectSnd = LoadSound("Sounds\select.wav")
	CheckSound(selectSnd, "Sounds\select.wav")
	
	menuSnd = LoadSound("Sounds\menu.wav")
	CheckSound(menuSnd, "Sounds\menu.wav")
	
	playerShootSnd = LoadSound("Sounds\playershoot.wav")
	CheckSound(playerShootSnd, "Sounds\playershoot.wav")
	
	collisionSnd = LoadSound("Sounds\collision.wav")
	CheckSound(collisionSnd, "Sounds\collision.wav")
	
	levelStartSnd = LoadSound("Sounds\levelstart.wav")
	CheckSound(levelStartSnd, "Sounds\levelstart.wav")
	
	levelEndSnd = LoadSound("Sounds\levelend.wav")
	CheckSound(levelEndSnd, "Sounds\levelend.wav")

End Function

;Displays a message for users whose computer is dragging its feet
Function DisplayLoadingMessage()

	Cls
	Text 20,40,"Loading..."
	Flip

End Function

Function ResetGame()

	Delete Each AlienData
	Delete Each SwarmData
	Delete Each BulletData
	Delete Each ExplosionData
	Delete Each SparkData
	Delete Each InGameMessageData
	
	InitPlayer()
	
	currentMission = 0
	currentGameState = GMSTATE_TITLE

	Color 0, 180, 0
	SetFont mainFont
	
End Function

Function ExitProgram()

	Janitor()
	End

End Function

Function Janitor()

	Delete Each PlayerData
	Delete Each AlienData
	Delete Each ImageData
	Delete Each SwarmData
	Delete Each BulletData
	Delete Each ExplosionData
	Delete Each SparkData
	Delete Each InGameMessageData
	
	FreeFont mainFont
	FreeFont largeFont
	FreeFont smallFont

End Function

Function Explode()

	;Repeat the following loop until the fade colour values have finally gone from 
	;black To yellow To white To black
	Repeat

		; Increase yellow colour value
		yellow = yellow + 5

		; If yellow colour value exceeds maximum (255), then start increasing white colour value
		If yellow > 255 Then yellow = 255 : white = white + 5

		; If white colour value exceeds maximum, then start increasing black colour value
		If white > 255 Then black = black + 5 : white = 255

		; Set clear screen colour
		ClsColor yellow - black, yellow - black, white - black

		; Clear screen
		Cls

		; Flip screen bufffers
		Flip

	Until black > 255
	ClsColor 0, 0, 0

End Function