Type SparkData
	Field x
	Field y
	Field curFrame
End Type

Function CreateSpark(x, y, frame = 0)
	
	sp.SparkData = New SparkData
	sp\x = x
	sp\y = y
	sp\curFrame = frame
	
End Function

Function UpdateSparks()

	i = 0

	For sp.SparkData = Each SparkData
		sp\curFrame = sp\curFrame + 1
		If sp\curFrame = spark\numOfFrames Then Delete sp
		i = i + 1
	Next
	
	Return i
	
End Function

Function RenderSparks()

	For sp.SparkData = Each SparkData
		DrawImage spark\image, sp\x, sp\y, sp\curFrame
	Next
	
End Function