Const SWITCH_OFF_SCENERY = -1

Type SceneryControlData
	Field randScenery
	Field x
	Field y
	Field speed
	Field displayTime
End Type

Type SceneryData
	Field x
	Field y
	Field frame
End Type

Global sceneryControls.SceneryControlData = New SceneryControlData

Function UpdateScenery()

	If sceneryControls\randScenery = SWITCH_OFF_SCENERY Then Return
	
	If sceneryControls\randScenery Then
		show = Rand(0,100)
		If Not show Then
			x = Rand(0, WIDTH - scenery\width) 
			y = 0 - scenery\height
			CreateScenery(x,y,(Rand(1, scenery\numOfFrames)) - 1)
		EndIf
	ElseIf sceneryControls\displayTime + missionStartTime <= currentGameTime Then
		x = sceneryControls\x
		y = sceneryControls\y
		CreateScenery(x,y,(Rand(1, scenery\numOfFrames)) - 1)
	EndIf

	For sn.SceneryData = Each SceneryData
		If sn\y > heightIncHUD
			Delete sn
		Else
			sn\y = sn\y + sceneryControls\speed
		EndIf
	Next	

End Function

Function CreateScenery(x, y, frame)

	s.SceneryData = New SceneryData

	s\x = x
	s\y = y
	s\frame = frame

End Function

Function RenderScenery()
	
	For s.SceneryData = Each SceneryData
		DrawImage scenery\image, s\x, s\y, s\frame
	Next
	
End Function