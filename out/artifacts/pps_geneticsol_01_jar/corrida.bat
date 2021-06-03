set nameFile=res
for /l %%x in (1, 1, 5) do (
	java -jar pps-geneticsol-01.jar dataset05 > %nameFile%%%x.txt
)