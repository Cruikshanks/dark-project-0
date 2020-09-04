Const WAVE_STATBAR_THICKNESS = 10

Function RenderHUD()
	
	DrawImage hud\image, 0, (HEIGHT - hud\height)
	
	DisplayExcess(130, 460, player\excess)
	DisplayShield(276, 465, player\shield)
	DisplayHull(451, 465, player\hull)
	DisplayCurrentBullet(600, 460)
	DisplayWaveStat(0,(HEIGHT - hud\height - WAVE_STATBAR_THICKNESS))

End Function

Function DisplayExcess(x,y,excess)

	Color 255,255,255
	Text x, y, excess, True, False

End Function

Function DisplayShield(x,y,shield)

	If shield > 0 Then
		If shield > 50 Then
			Color 0,255,0
		ElseIf shield <= 50 And shield > SHIELD_REDLINE Then
			Color 255,255,0
		Else
			Color 255,0,0
		EndIf
				
		Rect x, y, shield, 7, 1

	Else
		Color 255,0,0
		Text x+6,y-5,"NO SHIELD", False, False
	EndIf

End Function

Function DisplayHull(x,y,hull)

	Color 255,255,0

	For i = 0 To hull
		Rect x, y, 8, 7, 1
		x = x + 10
	Next
	
End Function

Function DisplayCurrentBullet(x,y)
	
	Select player\curBullet
	
		Case STD_BULLET
			DrawImage bullets\image, x, y, P_STD_BLT
		Case BOMB_BULLET
			DrawImage bullets\image, x, y, P_BOMB_BLT
		Case HOMING_BULLET
			DrawImage bullets\image, x, y, P_HOMING_BLT
			
	End Select
	
End Function

Function DisplayWaveStat(x,y)

	Color 1, 18, 64	
	Rect x, y, WIDTH, WAVE_STATBAR_THICKNESS, 1
	
	Local widthHUD# = hud\width
	unit# = widthHUD / aliensTotal

	If (aliensKilled + aliensPassed) Then
		aliensKilledBar# = unit * aliensKilled
		aliensPassedBar# = unit * aliensPassed
	
		Color 255, 0, 0
		Rect (widthHUD - aliensPassedBar), y, aliensPassedBar, WAVE_STATBAR_THICKNESS, 1
		
	EndIf
	
	Color 175, 175, 50
	Rect (((aliensTotal / 100) * missionWaveLevel) * unit), y, 3, WAVE_STATBAR_THICKNESS, 1
	
	SetFont smallFont
	tempString$ = "WAVE " + currentMission
	Text 0 + StringWidth(tempString), y-2, "WAVE " + currentMission, True, False
	
	SetFont mainFont
	
End Function