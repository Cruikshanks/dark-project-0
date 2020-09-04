Type BackdropControlData
	Field useTopLayer
	Field speed
	Field offset
End Type

Global backdropControls.BackdropControlData = New BackdropControlData
Global scrollMapY = 0

Function RenderBackdrop()

	TileBlock backdrop\image, 0, scrollMapY, BACKDROP_BOTTOM_LAYER
	
	If backdropControls\useTopLayer Then
TileImage backdrop\image,backdropControls\offset,scrollMapY * backdropControls\speed, BACKDROP_TOP_LAYER
	EndIf
	
	scrollMapY = (scrollMapY + 1) Mod backdrop\height
	
End Function