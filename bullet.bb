Const STD_BULLET = 1, BOMB_BULLET = 2, HOMING_BULLET = 3
Const ALIEN_BULLET = 1, PLAYER_BULLET = 2

Const STD_BLT_DMG_PLYER = 5, STD_BLT_DMG_ALIEN = 10
Const BMB_BLT_DMG_PLYER = 30, BMB_BLT_DMG_ALIEN = 30
Const HOM_BLT_DMG_PLYER = 20, HOM_BLT_DMG_ALIEN = 10

Type BulletData
	Field x
	Field y
	Field speed
	Field whoseBullet
	Field bulletType
	
	Field variable
	Field flightTime
End Type

Function CreateBullet(x,y,speed,whose,bltType,var,fltTime)

	b.bulletData  = New bulletData
	
	b\x = x
	b\y = y
	b\speed = speed
	b\whoseBullet = whose
	b\bulletType = bltType
	
	b\variable = var
	b\flightTime = fltTime
	
End Function

Function UpdateBullets()

	i = 0

	For b.BulletData = Each BulletData
	
		If b\whoseBullet = ALIEN_BULLET Then i = i + 1
		
		Select b\bulletType
	
			Case STD_BULLET
				UpdateStandardBullet(b)
			Case BOMB_BULLET
				UpdateBombBullet(b)
			Case HOMING_BULLET
				UpdateHomingBullet(b)
			
		End Select
	Next
	
	Return i
	
End Function
	
Function UpdateStandardBullet(b.BulletData)

	hit = 0
	Select b\whoseBullet
	
		Case PLAYER_BULLET
			For a.AlienData = Each AlienData
				hit = ImagesCollide(bullets\image,b\x,b\y,P_STD_BULLET,a\image,a\x,a\y,a\curFrame)
				If hit Then AlienKilledUpdate(a,STD_BLT_DMG_PLYER,0) : Exit
			Next

			If Not hit Then b\y = b\y - b\speed
			
			If hit Or (b\y + bullets\height) <= 0 Then Delete b
			
		Case ALIEN_BULLET
hit = ImagesCollide(bullets\image,b\x,b\y,A_STD_BULLET,player\image,player\x,player\y,player\curFrame)
			
			If Not hit Then 
				b\y = b\y + b\speed
			Else
				CreateSpark(b\x, b\y, 0)
				If player\shield <= SHIELD_REDLINE Then 
					player\hull = Min(player\hull - 1, 0)
					player\shield = Min(player\shield - STD_BLT_DMG_ALIEN, 0)
				Else 
					player\shield = Min(player\shield - STD_BLT_DMG_ALIEN, 0)
				EndIf
			EndIf
			
			If hit Or b\y >= heightIncHUD Then Delete b
			
	End Select	

End Function

Function UpdateBombBullet(b.bulletData)

	hit = 0
	Select b\whoseBullet
	
		Case PLAYER_BULLET
			For a.AlienData = Each AlienData
				hit = ImagesCollide(bullets\image,b\x,b\y,P_BOMB_BULLET,a\image,a\x,a\y,a\curFrame)
				If hit Then AlienKilledUpdate(a,BMB_BLT_DMG_PLYER,0) : Exit
			Next
			
			If Not hit Then
				b\y = b\y - (b\speed * 1.5)
			
				Select b\variable
					Case MOV_RIGHT
						b\x = b\x + (b\speed / 2)
					Case MOV_LEFT
						b\x = b\x - (b\speed / 2)
				End Select
			EndIf

			If hit Or (b\y + bullets\height) <= 0 Or b\x >= WIDTH Or (b\x + bullets\height) <= 0 Then 
				Delete b
			EndIf
			
		Case ALIEN_BULLET
hit = ImagesCollide(bullets\image,b\x,b\y,A_BOMB_BULLET,player\image,player\x,player\y,player\curFrame)
			
			If Not hit Then
				b\y = b\y + (b\speed * 1.5)
		
				Select b\variable
					Case MOV_RIGHT
						b\x = b\x + (b\speed / 2)
					Case MOV_LEFT
						b\x = b\x - (b\speed / 2)
				End Select
			Else
				CreateSpark(b\x, b\y, 0)
				If player\shield <= SHIELD_REDLINE Then 
					player\hull = Min(player\hull - 2, 0) 
					player\shield = Min(player\shield - BMB_BLT_DMG_ALIEN, 0)
				Else
					player\shield = Min(player\shield - BMB_BLT_DMG_ALIEN, 0)
				EndIf

			EndIf
		
			If hit Or b\y >= heightIncHUD Or b\x >= WIDTH Or (b\x + bullets\height) <=0 Then
				Delete b
			EndIf
			
	End Select
	
End Function

Function UpdateHomingBullet(b.bulletData)

	hit = 0
	Select b\whoseBullet
		
		Case PLAYER_BULLET

			Select b\variable
				
				Case MOV_START
					xDif = WIDTH
					yDif = HEIGHT
					currentDif = xDif * yDif
		
					For a.AlienData = Each AlienData
If hit = ImagesCollide(bullets\image,b\x,b\y,A_HOMING_BLT,a\image,a\x,a\y,a\curFrame) Then AlienKilledUpdate(a,5,0)
						If a\y < b\y Then
							xDif = Abs(b\x - a\x)
							yDif = Abs(b\y - a\y)
							If (xDif + yDif) < currentDif Then
								xDif = a\x : yDif = a\y
								x = a\x + (stdAlien\width / 2)
								y = a\y + (stdAlien\height / 2)
							EndIf
						EndIf
					Next
		
					If Not hit Then
						x = player\x + (plyNotFired\width / 2)
						y = player\y + (plyNotFired\height / 2)

						If b\x < xDif Then b\x = b\x + (b\speed/2) Else b\x = b\x - (b\speed/2)
						If b\y < yDif Then b\y = b\y - b\speed Else b\y = b\y + b\speed
					
						If b\flightTime < currentGameTime Then b\variable = MOV_FLEE
					EndIf
					
				Case MOV_FLEE
					For a.AlienData = Each AlienData
					hit = ImagesCollide(bullets\image,b\x,b\y,P_HOMING_BLT,a\image,a\x,a\y,a\curFrame)
						If hit Then AlienKilledUpdate(a,HOM_BLT_DMG_PLYER,0) : Exit
					Next
					
					If Not hit Then b\y = b\y - (b\speed * 5)
					
			End Select

			If hit Or (b\y + bullets\height) <= 0 Then Delete b
		
		Case ALIEN_BULLET
hit = ImagesCollide(bullets\image,b\x,b\y,A_HOMING_BLT,player\image,player\x,player\y,player\curFrame)
			
			If Not hit Then
				Select b\variable
		
					Case MOV_START
						x = player\x + (plyNotFired\width / 2)
						y = player\y + (plyNotFired\height / 2)
						If b\x < x Then b\x = b\x + b\speed Else b\x = b\x - b\speed
						b\y = b\y + b\speed
					
						If b\flightTime < currentGameTime Then b\variable = MOV_FLEE
					
					Case MOV_FLEE
						b\y = b\y + (b\speed * 5)
				
				End Select
			Else
				CreateSpark(b\x, b\y, 0)
				If player\shield <= SHIELD_REDLINE Then 
					player\hull = Min(player\hull - 1, 0)
					player\shield = Min(player\shield - HOM_BLT_DMG_ALIEN, 0)
					Else
					player\shield = Min(player\shield - HOM_BLT_DMG_ALIEN, 0)
				EndIf
			EndIf
				
			If hit Or b\y >= heightIncHUD Then Delete b
				
	End Select

End Function

Function RenderBullets()

	For b.BulletData = Each BulletData
	
		Select b\bulletType
	
			Case STD_BULLET
				RenderStandardBullet(b)
			Case BOMB_BULLET
				RenderBombBullet(b)
			Case HOMING_BULLET
				RenderHomingBullet(b)
			
		End Select
	Next
	
End Function

Function RenderStandardBullet(b.BulletData)

	If b\whoseBullet = PLAYER_BULLET Then
		DrawImage bullets\image, b\x, b\y, P_STD_BLT
	Else
		DrawImage bullets\image, b\x, b\y, A_STD_BLT
	EndIf

End Function

Function RenderBombBullet(b.bulletData)

	If b\whoseBullet = PLAYER_BULLET Then
		DrawImage bullets\image, b\x, b\y, P_BOMB_BLT
	Else
		DrawImage bullets\image, b\x, b\y, A_BOMB_BLT
	EndIf

End Function

Function RenderHomingBullet(b.bulletData)

	If b\whoseBullet = PLAYER_BULLET Then
		DrawImage bullets\image, b\x, b\y, P_HOMING_BLT
	Else
		DrawImage bullets\image, b\x, b\y, A_HOMING_BLT
	EndIf

End Function