Const NUM_OF_EXPLOSIONS = 10

Type ExplosionData
	Field x
	Field y
	Field curFrame
End Type

Function CreateExplosion(x, y, frame = 0)
	
	e.ExplosionData = New ExplosionData
	e\x = x
	e\y = y
	e\curFrame = frame
	
End Function

Function UpdateExplosions()

	i = 0

	For e.ExplosionData = Each ExplosionData
		e\curFrame = e\curFrame + 1
		If e\curFrame = (explosion\numOfFrames * 3) Then Delete e
		i = i + 1
	Next
	
	Return i
	
End Function

Function RenderExplosions()

	For e.ExplosionData = Each ExplosionData
		DrawImage explosion\image, e\x, e\y, (e\curFrame / 3)
	Next
	
End Function