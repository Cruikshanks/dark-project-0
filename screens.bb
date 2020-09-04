Const START_SELECTED = 1, MBRIEF_SELECTED = 2, OPTIONS_SELECTED = 3
Const ABOUT_SELECTED = 4, EXIT_SELECTED = 5
Const NUM_OF_OPTIONS = 5

Global optionSelected

Function DisplaySplashScreens()

	image = LoadImage("Graphics\startup.bmp")
	CheckImage(image, "Graphics\startup.bmp")
	loadingSnd = LoadSound("Sounds\loading.wav")
	CheckSound(loadingSnd, "Sounds\loading.wav")
	
	Cls
	DrawImage image,(WIDTH/2) - 240,(HEIGHT/2) - 90
	Flip
	
	Delay(2 * ONESECOND)
	PlaySound loadingSnd
	Delay(2 * ONESECOND)
	
End Function

Function DisplayStartScreen()

	SetFont largeFont

	Cls

	DrawImage screens\image, 0, 0, SCREEN_START
	
	If optionSelected = START_SELECTED Then
		Color 255,0,0
		Text 505, 335, "START", True, False
	Else
		Color 175, 175, 50
		Text 505, 335, "START", True, False
	EndIf
	If optionSelected = MBRIEF_SELECTED Then
		Color 255,0,0
		Text 505, 360, "MISSION BRIEF", True, False
	Else
		Color 175, 175, 50
		Text 505, 360, "MISSION BRIEF", True, False
	EndIf
	If optionSelected = OPTIONS_SELECTED Then
		Color 255,0,0
		Text 505, 385, "OPTIONS", True, False
	Else
		Color 175, 175, 50
		Text 505, 385, "OPTIONS", True, False
	EndIf
	If optionSelected = ABOUT_SELECTED Then
		Color 255,0,0
		Text 505, 410, "ABOUT DP0", True, False
	Else
		Color 175, 175, 50
		Text 505, 410, "ABOUT DP0", True, False
	EndIf
	If optionSelected = EXIT_SELECTED Then
		Color 255,0,0
		Text 505, 435, "EXIT", True, False
	Else
		Color 175, 175, 50
		Text 505, 435, "EXIT", True, False
	EndIf

	Flip
	
	SetFont mainFont
	Color 0, 180, 0

	If KeyHit(200) Or KeyHit(72) Then ;Up cursor
		PlaySound menuSnd
		optionSelected = optionSelected - 1
		If optionSelected < START_SELECTED Then optionSelected = EXIT_SELECTED
	EndIf
	If KeyHit(208) Or KeyHit(80) Then ;Down cursor
		PlaySound menuSnd
		optionSelected = optionSelected + 1
		If optionSelected > NUM_OF_OPTIONS Then optionSelected = START_SELECTED
	EndIf
	
	If KeyHit(28) Or KeyHit(157) Or KeyHit(29) Then 

		PlaySound selectSnd
		Select optionSelected
		
			Case START_Selected : BeginMission()
			Case MBRIEF_SELECTED : MissionBrief()
			Case OPTIONS_SELECTED : DisplayOptions()
			Case ABOUT_SELECTED : DisplayAbout()
			Case EXIT_SELECTED : ExitProgram()
		End Select
	EndIf
	
End Function

Function DisplayOptions()

	image = CreateImage(WIDTH, HEIGHT)
	SetBuffer ImageBuffer(image)
	
	x = WIDTH / 2
	y = 20
	
	Restore options
	Repeat
		Read readMsg$
		Select readMsg
			Case "LARGE"
				SetFont largeFont
			Case "MID"
				SetFont mainFont
			Case "SMALL"
				SetFont smallFont
			Case "COLOR"
				Read r : Read g : Read b
				Color r, g, b
			Case "999"
			Default
				Text x, y, readMsg, True, False
				Read spacer
				y = y + FontHeight() + spacer
		End Select
	Until readMsg = "999"

	SetBuffer BackBuffer()
	Cls
	DrawImage image, 0,0
	Flip
	
	FlushKeys
	WaitKey
	FlushKeys

End Function

Function DisplayAbout()

	image = CreateImage(WIDTH, HEIGHT)
	SetBuffer ImageBuffer(image)
	
	x = WIDTH / 2
	y = 10
	
	Restore about
	Repeat
		Read readMsg$
		Select readMsg
			Case "LARGE"
				SetFont largeFont
			Case "MID"
				SetFont mainFont
			Case "SMALL"
				SetFont smallFont
			Case "COLOR"
				Read r : Read g : Read b
				Color r, g, b
			Case "999"
			Default
				Text x, y, readMsg, True, False
				Read spacer
				y = y + FontHeight() + spacer
		End Select
	Until readMsg = "999"

	SetBuffer BackBuffer()
	ClsColor 255,255,255
	Cls
	DrawImage image, 0,0
	Flip
	
	ClsColor 0,0,0
	FlushKeys
	WaitKey
	FlushKeys

End Function

Function DisplayEndOfLevelScreen()

	PlaySound levelEndSnd
	FlushKeys
	
	waveStat# = (100 / (aliensTotal)) * aliensKilled
	If waveStat < missionWaveLevel Then player\isDead = True : currentGameState = GMSTATE_ENDGAME

	test1 = False : test2 = False
	Repeat
		currentGameTime = MilliSecs()
		
		If Not test1 Then test1 = MovePlayerTo((WIDTH/2)-(plyNotFired\width/2),0-plyNotFired\height)
		If Not test2 Then test2 = UpdateShip()
		
		UpdateBullets()
		UpdateScenery()
		
		Cls
		RenderGame()
		DisplayPlayerStats(test2, waveStat)
		Flip
		
		If test1 And test2 Then
			If KeyHit(157) Or KeyHit (29) Or KeyHit (28) Then Exit
		EndIf
	Forever
	
	FlushKeys

	If player\isDead Then Return

	Delete Each AlienData
	Delete Each SwarmData
	Delete Each BulletData
	Delete Each ExplosionData
	Delete Each SparkData
	Delete Each InGameMessageData
	
	ResetPlayerPosition()
	BeginMission()
	
End Function

Function DisplayPlayerStats(recharged, waveStat#)

	SetFont largeFont
	x = WIDTH / 2

	If Not recharged Then 
		Color 255, 0, 0
		Text x, 150, "PERFORMING REPAIRS", True, False
	Else
		Color 0, 180, 0
		Text x, 150, "REPAIRS COMPLETE - PRESS FIRE WHEN READY", True, False
	EndIf
		
	Color 175, 175, 50
	Text x, 180, "Excess used - " + (player\totalExcess - player\excess), True, False
	Text x, 200, "Aliens killed - " + Int(waveStat) + "%", True, False
	Text x, 220, "Aliens escaped - " + Int(100 - waveStat) + "%", True, False
	
	If waveStat < missionWaveLevel Then
		Color 255, 0, 0
		Text x, 250, "YOU HAVE FAILED! EARTH IS DOOMED!", True, False
	ElseIf Abs(missionWaveLevel - waveStat) <= 10 Then
		Text x, 250, "Careful, you cut that last one a little close", True, False
	Else
		Text x, 250, "More like that and Earth will stand a chance", True, False
	EndIf
	
	SetFont mainFont

End Function

Function DisplayEndOfGameScreen()

	FlushKeys

	If player\isDead And player\hull <= 0 Then
		PlaySound playerDeathSnd 
		Explode()
		Cls
		DrawImage screens\image, 0, 0, SCREEN_DIED
		Flip
	ElseIf player\isDead Then
		PlaySound playerDeathSnd
		i.ImageData = New ImageData
		CreateEndGameMessage(i, False)
		scroll# = HEIGHT
		While Not scroll# <= 0
			Cls
			DrawImage screens\image, 0, 0, SCREEN_LOST
			
			If KeyHit(157) Then scroll# = 0
			
			DrawImage i\image, 0, scroll#
			scroll# = (scroll# - 0.5) Mod i\height
			Flip
		Wend
	Else
		i.ImageData = New ImageData
		CreateEndGameMessage(i, True)
		scroll# = HEIGHT
		While Not scroll# <= 0
			Cls
			DrawImage screens\image, 0, 0, SCREEN_WON
			
			If KeyHit(157) Then scroll# = 0
			
			DrawImage i\image, 0, scroll#
			scroll# = (scroll# - 0.5) Mod i\height
			Flip
		Wend
	EndIf
	
	FlushKeys
	WaitKey
	FlushKeys

End Function