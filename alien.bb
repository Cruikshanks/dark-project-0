Const STD_ALIEN = 1, BOMBER_ALIEN = 2, ACE_ALIEN = 3
Const MOV_LEFT = 1, MOV_RIGHT = 2, MOV_UP = 3, MOV_DOWN = 4, MOV_START = 5, MOV_FLEE = 6

Type AlienData
	Field alienType
	Field x
	Field y
	Field speed
	Field hull
	Field canFire
	Field curBullet
	Field flightTime
	Field isDead
	
	Field startX
	Field startY
	Field movType
	Field offset
	
	Field curFrame
	Field image
	
	Field leftBoundary
	Field rightBoundary
	Field topBoundary
	Field bottomBoundary
End Type

Global aliensTotal# = 0
Global aliensPassed# = 0
Global aliensKilled# = 0

Function UpdateAliens()

	numOfAliens = 0
	
	For a.AlienData = Each AlienData
	
		If a\y >= heightIncHUD Then
			aliensPassed = aliensPassed + 1
			Delete a
		ElseIf a\isDead Then
			aliensKilled = aliensKilled + 1
			CreateExplosion(a\x, a\y, 0)
			PlaySound alienDeathSnd
			Delete a
		Else
			Select a\alienType
		
				Case STD_ALIEN
					UpdateStandardAlien(a)

				Case BOMBER_ALIEN
					UpdateBomberAlien(a)

				Case ACE_ALIEN
					UpdateAceAlien(a)
				
			End Select
		EndIf
				
		numOfAliens = numOfAliens + 1
	Next
	
	Return numOfAliens
	
End Function

Function AlienKilledUpdate(a.AlienData, bulletStrength, collided = 0)

	a\hull = Min(a\hull - bulletStrength, 0)

	Select a\alienType
		
		Case STD_ALIEN
			If collided Then
				PlaySound collisionSnd
				a\isDead = True
				player\excess = player\excess + 50
				player\totalExcess = player\totalExcess + 50
				If player\shield <= SHIELD_REDLINE Then 
					player\hull = Min(player\hull - 1, 0)
					player\shield = Min(player\shield - 20, 0)
				Else
					player\shield = Min(player\shield - 20, 0)
				EndIf
			ElseIf a\hull <= 0 Then
				a\isDead = True
				player\excess = player\excess + 100
				player\totalExcess = player\totalExcess + 100
			EndIf
			
		Case BOMBER_ALIEN
			If collided Then
				PlaySound collisionSnd
				a\isDead = True
				player\excess = player\excess + 100
				player\totalExcess = player\totalExcess + 100
				If player\shield <= SHIELD_REDLINE Then 
					player\hull = Min(player\hull - 2, 0)
					player\shield = Min(player\shield - 30, 0)
				Else 
					player\shield = Min(player\shield - 30, 0)
				EndIf
			ElseIf a\hull <= 0 Then
				a\isDead = True
				player\excess = player\excess + 200
				player\totalExcess = player\totalExcess + 200
			EndIf
			
		Case ACE_ALIEN
			If collided Then
				PlaySound collisionSnd
				a\isDead = True
				player\excess = player\excess + 150
				player\totalExcess = player\totalExcess + 150
				If player\shield <= SHIELD_REDLINE Then 
					player\hull = Min(player\hull - 1, 0)
					player\shield = Min(player\shield - 20, 0)
				Else 
					player\shield = Min(player\shield - 20, 0)
				EndIf
			ElseIf a\hull <= 0 Then
				a\isDead = True
				player\excess = player\excess + 300
				player\totalExcess = player\totalExcess + 300
			EndIf
			
	End Select

End Function

Function RenderAliens()

	For a.AlienData = Each AlienData
	
		Select a\alienType
		
			Case STD_ALIEN
				RenderStandardAlien(a)
			Case BOMBER_ALIEN
				RenderBomberAlien(a)
			Case ACE_ALIEN
				RenderAceAlien(a)
				
		End Select
		
	Next

End Function

Function CreateStandardAlien(x,y,speed,flightTime,stFrame,image,startX,startY)

	a.AlienData = New AlienData
	a\alienType = STD_ALIEN

	a\x = x
	a\y = y
	a\speed = speed
	a\hull = 5
	a\canFire = False
	a\curBullet = STD_BULLET
	a\flightTime = flightTime
	a\isDead = False
	
	a\curFrame = stFrame
	a\image = image
	
	a\startX = startX
	a\startY = startY	
	a\movType = MOV_START
	a\offset = 100
	
	a\rightBoundary = WIDTH - stdAlien\width
	a\leftBoundary = 0
	a\topBoundary = 0
	a\bottomBoundary = heightIncHUD

End Function
	
Function CreateBomberAlien(x,y,speed,flightTime,stFrame,image,startX,startY)
	
	a.AlienData = New AlienData
	a\alienType = BOMBER_ALIEN
	
	a\x = x
	a\y = y
	a\speed = speed
	a\hull = 5
	a\canFire = False
	a\curBullet = BOMB_BULLET
	a\flightTime = flightTime
	a\isDead = False
	
	a\curFrame = stFrame
	a\image = image
	
	a\startX = startX
	a\StartY = startY	
	a\movType = MOV_START
	a\offset = 0
		
	a\rightBoundary = WIDTH - bombAlien\width
	a\leftBoundary = 0
	a\topBoundary = 0
	a\bottomBoundary = heightIncHUD
	
End Function

Function CreateAceAlien(x,y,speed,flightTime,stFrame,image,startX,startY)

	a.AlienData = New AlienData
	a\alienType = ACE_ALIEN
	
	a\x = x
	a\y = y
	a\speed = speed
	a\hull = 15
	a\canFire = False
	a\curBullet = HOMING_BULLET
	a\flightTime = flightTime
	a\isDead = False
	
	a\curFrame = stFrame
	a\image = image
	
	a\startX = startX
	a\startY = startY
	a\movType = MOV_START
	a\offset = 100
		
	a\rightBoundary = WIDTH - aceAlien\width
	a\leftBoundary = 0
	a\topBoundary = 0
	a\bottomBoundary = heightIncHUD

End Function 

Function UpdateStandardAlien(a.AlienData)

	If ImagesCollide(a\image,a\x,a\y,a\curFrame,player\image,player\x,player\y,player\curframe) Then
		AlienKilledUpdate(a, 0, 1)
		Return
	EndIf
	
	Select a\movType
		
		Case MOV_RIGHT
			a\x = Max(a\x + a\speed, a\rightBoundary)
			a\offset = a\offset - a\speed
			If a\offset <= 0 Then a\movType = MOV_LEFT : a\offset = 100
		
		Case MOV_LEFT
			a\x = Min(a\x - a\speed, a\leftBoundary)
			a\offset = a\offset - a\speed
			If a\offset <= 0 Then a\movType = MOV_RIGHT : a\offset = 100
		
		Case MOV_START
			If a\x < a\startX Then 
				a\x = Max(a\x + a\speed * 3, a\startX)
			Else 
				a\x = Min(a\x - a\speed * 3, a\startX)
			EndIf
			If a\y < a\startY Then 
				a\y = Max(a\y + a\speed * 3, a\startY)
			Else
				a\y = Min(a\y - a\speed * 3, a\startY)
			EndIf
						
			If a\x >= a\startX And a\y >= a\startY Then
				a\x = a\startX : a\y = a\startY
				a\movType = MOV_RIGHT
				a\canfire = currentGameTime + (ONESECOND / 2)
			EndIf

		Case MOV_FLEE
			a\y = a\y + a\speed
	End Select
	
	If a\canFire 
		If a\canfire < currentGameTime
			If Rand(0, 5) Then
				a\canFire = currentGameTime + (ONESECOND / 2)
			Else
				x = (a\x + (stdAlien\width / 2)) - (bullets\width / 2)
				y = a\y + stdAlien\height
				CreateBullet(x,y,3,ALIEN_BULLET,STD_BULLET,0,0)
			EndIf
		EndIf
	EndIf
	
	If currentGameTime > a\flightTime Then a\movType = MOV_FLEE : a\canFire = False
	a\curFrame = (a\curFrame + 1) Mod stdAlien\numOfFrames

End Function

Function UpdateBomberAlien(a.AlienData)

	If ImagesCollide(a\image,a\x,a\y,a\curFrame,player\image,player\x,player\y,player\curframe) Then
		AlienKilledUpdate(a, 0, 1)
		Return
	EndIf

	Select a\movType
		
		Case MOV_DOWN
			a\y = a\y + a\speed
		Case MOV_START
			a\movType = MOV_DOWN
			a\canFire = currentGameTime + ONESECOND
		Case MOV_FLEE
			a\y = a\y + (a\speed * 3)
		
	End Select
	
	If a\canFire 
		If a\canfire < currentGameTime Then
			a\canFire = currentGameTime + (ONESECOND / 2)
			x = (a\x + (bombAlien\width / 2)) - (bullets\width / 2)
			y = a\y + bombAlien\height
			
			If player\x >= a\x Then variable = MOV_RIGHT Else variable = MOV_LEFT
			
			CreateBullet(x,y,2,ALIEN_BULLET,BOMB_BULLET,variable,0)
		EndIf
	EndIf
	
	If currentGameTime > a\flightTime Then a\movType = MOV_FLEE : a\canFire = False
	a\curFrame = (a\curFrame + 1) Mod bombAlien\numOfFrames

End Function

Function UpdateAceAlien(a.AlienData)

	If ImagesCollide(a\image,a\x,a\y,a\curFrame,player\image,player\x,player\y,player\curframe) Then
		AlienKilledUpdate(a, 0,1)
		Return
	EndIf

	Select a\movType
	
		Case MOV_RIGHT
			a\x = Max(a\x + a\speed, a\rightBoundary)
			a\offset = a\offset - a\speed
			If a\offset <= 0 Then
				a\offset = 100
				If player\x >= a\x Then
					a\movType = MOV_RIGHT
				Else
					a\movType = MOV_LEFT
				EndIf
			EndIf
			
		Case MOV_LEFT
			a\x = Min(a\x - a\speed, a\leftBoundary)
			a\offset = a\offset - a\speed
			If a\offset <= 0 Then
				a\offset = 100
				If player\x >= a\x Then
					a\movType = MOV_RIGHT
				Else
					a\movType = MOV_LEFT
				EndIf
			EndIf
		
		Case MOV_START
			If a\y < a\startY Then a\y = Max(a\y + a\speed * 1.5, a\startY)
						
			If a\y >= a\startY Then
				If player\x >= a\x Then
					a\movType = MOV_RIGHT
				Else
					a\movType = MOV_LEFT
				EndIf

				a\y = a\startY
				a\canfire = currentGameTime + (1.5 * ONESECOND)
			EndIf
			
		Case MOV_FLEE
			If a\x > player\x Then 
				a\x = Max(a\x + a\speed, a\rightBoundary)
			Else 
				a\x = Min(a\x - a\speed, a\leftBoundary)
			EndIf

			a\y = a\y + a\speed
		
	End Select
	
	If a\canFire 
		If a\canfire < currentGameTime Then
			a\canFire = currentGameTime + (1.5 * ONESECOND)
			x = (a\x + (aceAlien\width / 2)) - (bullets\width / 2)
			y = a\y + aceAlien\height
			time = currentGameTime + (3 * ONESECOND)
			CreateBullet(x,y,2,ALIEN_BULLET,HOMING_BULLET,MOV_START,time)
		EndIf
	EndIf
	
	If currentGameTime > a\flightTime Then a\movType = MOV_FLEE
	a\curFrame = (a\curFrame + 1) Mod aceAlien\numOfFrames

End Function

Function RenderStandardAlien(a.AlienData)

	DrawImage a\image, a\x, a\y, a\curFrame / 2
	
End Function

Function RenderBomberAlien(a.AlienData)

	DrawImage a\image, a\x, a\y, a\curFrame / 2
	
End Function

Function RenderAceAlien(a.AlienData)

	DrawImage a\image, a\x, a\y, a\curFrame / 2
	
End Function