Dim character%(10)

Global missionName
Global missionStartTime
Global missionWaveLevel
Global currentMission
Global randSceneryOn, sceneryXPos, sceneryYPos

Function BeginMission()

	currentGameState = GMSTATE_INGAME
	aliensTotal = 0
	aliensPassed = 0
	aliensKilled = 0
	missionWaveLevel = 0
	currentMission = currentMission + 1
	
	Select currentMission
	
		Case 1
			Mission1()
			PlaySound levelStartSnd
		Case 2
			Mission2()
			PlaySound levelStartSnd

		Default
			currentGameState = GMSTATE_ENDGAME
			
	End Select
	
	If missionWaveLevel = 0 Then missionWaveLevel = 50
	
	missionStartTime = currentGameTime
	player\timer = currentGameTime + ONESECOND
	
End Function

Function Mission1()

	missionWaveLevel = 80

	LoadGameImage(backdrop,"graphics\stars.bmp",200,200,2,0,0,0,0)
	backdropControls\useTopLayer = True
	backdropControls\speed = 2
	backdropControls\offset = 15
	
	LoadGameImage(scenery,"graphics\gasClouds.bmp",132,133,5,0,0,0,0)
	sceneryControls\randScenery = True
	sceneryControls\speed = 4
	
	gameTime = 0
	
	gameTime = gameTime + CreateInGameMessage(100,20,3,3,15,25,0,True,character%(4),MSG_1,0,2)
	
	gameTime = gameTime + 1
	CreateSwarm(STD_ALIEN, 15, 5, 2, 20, 30, 80, 10, (10 * ONESECOND), gameTime)
	
	gameTime = gameTime + 2
	x = (WIDTH/2)-(aceAlien\width/2)
	y = (0-aceAlien\height)+132
	CreateSwarm(ACE_ALIEN, 1, 1, 4, x, y, 20, 0, (10 * ONESECOND), gameTime)
		
	gameTime = gameTime + 2
	For i = 1 To 10
		gameTime = gameTime + 2
		x = Rand(bombAlien\width,WIDTH-(2*bombAlien\width))
		CreateSwarm(BOMBER_ALIEN, 1, 1, 2, x, 0, 20, 0, (2 * ONESECOND), gameTime)
	Next
	
End Function

Function Mission2()

	missionWaveLevel = 70

	LoadGameImage(backdrop,"graphics\stars.bmp",200,200,2,0,0,0,0)
	backdropControls\useTopLayer = True
	backdropControls\speed = 2
	backdropControls\offset = 15
	
	LoadGameImage(scenery,"graphics\gasClouds.bmp",132,133,5,0,0,0,0)
	sceneryControls\randScenery = True
	sceneryControls\speed = 4
	
	gameTime = 0
	
	gameTime = gameTime + CreateInGameMessage(100,20,3,3,15,25,0,True,character%(2),MSG_2,0,2)
	
	;first wave
	gameTime = gameTime + 1
	CreateSwarm(STD_ALIEN, 15, 5, 3, 20, 30, 80, 10, (10 * ONESECOND), gameTime)
	
	gameTime = gameTime + 2
	x = (WIDTH/2)-(aceAlien\width/2)
	y = (0-aceAlien\height)+132
	CreateSwarm(ACE_ALIEN, 1, 1, 4, x, y, 20, 0, (10 * ONESECOND), gameTime)
	
	;second wave
	gameTime = gameTime + 10
	CreateSwarm(STD_ALIEN, 15, 5, 3, 20, 30, 80, 10, (7 * ONESECOND), gameTime)
	
	gameTime = gameTime + 2
	x = (WIDTH/2)-(aceAlien\width/2)
	y = (0-aceAlien\height)+132
	CreateSwarm(ACE_ALIEN, 1, 1, 4, x, y, 20, 0, (7 * ONESECOND), gameTime)
	
	;third wave
	gameTime = gameTime + 10
	CreateSwarm(STD_ALIEN, 15, 5, 3, 20, 30, 80, 10, (5 * ONESECOND), gameTime)
	
	gameTime = gameTime + 2
	x = (WIDTH/2)-(aceAlien\width/2)
	y = (0-aceAlien\height)+132
	CreateSwarm(ACE_ALIEN, 1, 1, 4, x, y, 20, 0, (5 * ONESECOND), gameTime)

	;fourth wave	
	gameTime = gameTime + 10
	x = (WIDTH/2)-(aceAlien\width/2)
	y = (0-aceAlien\height)+132
	CreateSwarm(ACE_ALIEN, 1, 1, 4, x, y, 20, 0, (5 * ONESECOND), gameTime)
	
	gameTime = gameTime + 1
	CreateSwarm(STD_ALIEN, 5, 5, 1, 20, 30, 80, 10, (1 * ONESECOND), gameTime)
		
	gameTime = gameTime + 1
	x = (WIDTH/2)-(aceAlien\width/2)
	y = (0-aceAlien\height)+132
	CreateSwarm(ACE_ALIEN, 1, 1, 4, x, y, 20, 0, (5 * ONESECOND), gameTime)
	
	gameTime = gameTime + 1
	CreateSwarm(STD_ALIEN, 5, 5, 1, 20, 30, 80, 10, (1 * ONESECOND), gameTime)
	
	gameTime = gameTime + 1
	x = (WIDTH/2)-(aceAlien\width/2)
	y = (0-aceAlien\height)+132
	CreateSwarm(ACE_ALIEN, 1, 1, 4, x, y, 20, 0, (5 * ONESECOND), gameTime)
	
	gameTime = gameTime + 1
	CreateSwarm(STD_ALIEN, 5, 5, 1, 20, 30, 80, 10, (1 * ONESECOND), gameTime)

	;fifthwave
	gameTime = gameTime + 2
	j = 1
	For i = 1 To 10
		gameTime = gameTime + 1
		x = Rand(bombAlien\width,WIDTH-(2*bombAlien\width))
		CreateSwarm(BOMBER_ALIEN, 1, 1, 2, x, 0, 20, 0, (2 * ONESECOND), gameTime)
		
		If j = 2 Then CreateSwarm(STD_ALIEN, 5, 5, 1, 20, 30, 80, 10, (1 * ONESECOND), gameTime) : j=0
		j = j+1
	Next

End Function


Function MissionBrief()

	FlushKeys
	SetBuffer FrontBuffer()
	Restore missionbrief
	
	Repeat
		Cls
		Color 0, 180, 0
		SetFont mainfont
		
		Read num
		If Not num = 999 Then
			cnt = num
			For i = 1 To cnt
				m.InGameMessageData = New InGameMessageData
				Read num : m\x = num
				Read num : m\y = num
				Read num : m\numOfLines = num
				Read num : m\imageSpacer = num
				Read num : m\lineSpacer = num
				Read num : m\charPrintDelay = num
				Read num : m\quickPrint = num
				Read num : m\lineWrap = num
				Read num : m\image = character%(num)
				Read num : m\displayDelay = num

				Dim inGameMessage(m\numOfLines)
				For j = 1 To m\numOfLines
					Read msg_read$
					inGameMessage(j-1) = msg_read
				Next
				
				DisplayInGameMessage(m) : Delay(m\displayDelay*ONESECOND)
				Delete m
				If KeyHit(1) Then num = 999 : Exit
			Next
			SetFont smallfont	
			Color 175, 175, 50
			Text 320, 460, "Press Enter", True, False 
			WaitKey
			PlaySound selectSnd
			If KeyHit(1) Then Exit
			FlushKeys
		EndIf
	Until num = 999
	
	Delete Each InGameMessageData
	SetBuffer BackBuffer()
	FlushKeys

End Function