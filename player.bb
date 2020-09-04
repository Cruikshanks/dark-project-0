Const MAX_SHIELD = 100, MAX_HULL = 10
Const SHIELD_REGEN_COST = 20, HULL_REGEN_COST = 200
Const SHIELD_REDLINE = 25

Type PlayerData
	Field x
	Field y
	Field speed
	Field excess
	Field shield
	Field hull
	Field isDead
	
	Field timer
	Field curBullet
	Field curFrame
	Field image
	Field totalExcess
	
	Field leftBoundary
	Field rightBoundary
	Field topBoundary
	Field bottomBoundary
End Type

Global initPlayerSpeed = 4

Global player.PlayerData = New PlayerData

Function InitPlayer()

	player\x = (WIDTH / 2) - (plyNotFired\width / 2)
	player\y = heightIncHUD - plyNotFired\height
	player\speed = initPlayerSpeed
	player\excess = 0
	player\shield = MAX_SHIELD
	player\hull = MAX_HULL
	player\isDead = False
	
	player\curBullet = STD_BULLET
	player\curFrame = plyNotFired\startFrame
	player\image = plyNotFired\image
	player\totalExcess = 0
	
	player\leftBoundary = 0
	player\rightBoundary = WIDTH - plyNotFired\width
	player\topBoundary = 0
	player\bottomBoundary = heightIncHUD - plyNotFired\height
	
End Function

Function UpdatePlayer()

	rollPlayer = False
	player\image = plyNotFired\image
		
	;steer player
	If KeyDown(203) Or KeyDown(75) Then ;Left cursor
		player\x = Min((player\x - player\speed), player\leftBoundary)
		player\curFrame = Min((player\curFrame - 1), 0)
		rollPlayer = True
	EndIf
	If KeyDown(205) Or KeyDown(77) Then ;Right cursor
		player\x = Max((player\x + player\speed), player\rightBoundary)
		player\curFrame = Max((player\curFrame + 1), (plyNotFired\numOfFrames - 1))
		rollPlayer = True
	EndIf
	If KeyDown(200) Or KeyDown(72) Then ;Up cursor
		player\y = Min((player\y - player\speed), player\topBoundary)
	EndIf
	If KeyDown(208) Or KeyDown(80) Then ;Down cursor
		player\y = Max((player\y + player\speed), player\bottomBoundary)
	EndIf
	
	If KeyHit(157) Or KeyHit(29) Then
		PlaySound playerShootSnd 
		PlayerFiredBullet(player\curBullet)
		player\image = plyFired\image
	EndIf
		
	If rollPlayer = False
		If player\curFrame > plyFired\startFrame Then 
			player\curFrame = Min((player\curFrame - 1), 0)
		ElseIf player\curFrame < plyNotFired\startFrame Then 
			player\curFrame = Max((player\curFrame + 1), (plyNotFired\numOfFrames - 1))
		EndIf
	EndIf
			
End Function

Function UpdateShip()

	If player\timer < currentGameTime Then
		If currentGameState = GMSTATE_INGAME Then 
			player\timer = player\timer + (ONESECOND / 6)
		Else
			player\timer = player\timer + (ONESECOND/20)
		EndIf
			
		If player\hull = 0 Then
			player\isDead = True
			Return True
		EndIf
		
		If player\hull < MAX_HULL And player\excess >= HULL_REGEN_COST Then
			player\hull = Max(player\hull + 1, MAX_HULL)
			player\excess = Min(player\excess - HULL_REGEN_COST, 0)
		ElseIf player\shield < MAX_SHIELD And player\excess >= SHIELD_REGEN_COST Then
			player\shield = Max(player\shield + 1, MAX_SHIELD)
			player\excess = Min(player\excess - SHIELD_REGEN_COST, 0)
		EndIf
	EndIf
	
	If player\hull = MAX_HULL And player\shield = MAX_SHIELD Then Return True
	If player\excess < HULL_REGEN_COST And player\hull < MAX_HULL Then Return True
	If player\excess < SHIELD_REGEN_COST And player\shield < MAX_SHIELD Then Return True
	If player\excess <= 0 Then Return True
	
	Return False

End Function

Function PlayerFiredBullet(bulletType)

	Select bulletType
	
		Case STD_BULLET
			x = (player\x + (plyFired\width / 2)) - ((bullets\width / 2) + 3)

			CreateBullet(x, player\y, 5, PLAYER_BULLET, STD_BULLET, 0, 0)
			
		Case BOMB_BULLET
			x = player\x + ((plyFired\width / 2) - ((bullets\width / 2)) + 3)

			xDif = WIDTH
			yDif = HEIGHT
			currentDif = xDif * yDif
		
			For a.AlienData = Each AlienData
				If a\y < player\y Then
					xDif = Abs(player\x - a\x)
					yDif = Abs(player\y - a\y)
					If (xDif + yDif) < currentDif Then 
						xDif = a\x
					EndIf
				EndIf
			Next
			
			If xDif >= player\x Then variable = MOV_RIGHT Else variable = MOV_LEFT

			CreateBullet(x, player\y, 2, PLAYER_BULLET, BOMB_BULLET, variable, 0)

		Case HOMING_BULLET
			x = player\x + ((plyFired\width / 2) - ((bullets\width / 2)) + 3)
			time = currentGameTime + (3 * ONESECOND)
			
			CreateBullet(x, player\y, 2, PLAYER_BULLET, HOMING_BULLET, MOV_START, time)

	End Select
	
End Function

Function RenderPlayer()

	DrawImage player\image, player\x, player\y, player\curFrame

End Function

Function ResetPlayerPosition()

	player\x = (WIDTH / 2) - (plyNotFired\width / 2)
	player\y = heightIncHUD - plyNotFired\height
	player\curFrame = plyNotFired\startFrame
	
End Function

Function MovePlayerTo(moveToX, moveToY)

	If player\curFrame > plyFired\startFrame Then 
		player\curFrame = Min((player\curFrame - 1), 0)
	ElseIf player\curFrame < plyNotFired\startFrame Then 
		player\curFrame = Max((player\curFrame + 1), (plyNotFired\numOfFrames - 1))
	EndIf

	If player\x > moveToX Then 
		player\x = Min(player\x - player\speed, moveToX)
	Else 
		player\x = Max(player\x + player\speed, moveToX)
	EndIf
	
	If player\y > moveToY Then 
		player\y = Min(player\y - player\speed, moveToY)
	Else
		player\y = Max(player\y + player\speed, moveToY)
	EndIf
	
	If player\x <= moveToX And player\y <= moveToY Then
		Return True
	Else 
		Return False
	EndIf
	
End Function