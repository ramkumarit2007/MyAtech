arel.sceneReady(function()
{
	console.log("sceneReady");
	
    var munich = new arel.LLA(48.160879, 11.552156, 0);
	var london = new arel.LLA(51.50661, -0.130463, 0);
	var tokyo = new arel.LLA(35.657464, 139.773865, 0);
	var paris = new arel.LLA(48.85658, 2.348671, 0);
	var rome = new arel.LLA(41.90177, 12.45987, 0);

	arel.Scene.getObject("1").setLocation(munich);
    arel.Scene.getObject("1").setVisibility(true,false,true);
	arel.Scene.getObject("1").setLLALimitsEnabled(true);
	arel.Scene.setLLAObjectRenderingLimits(1000,1200);

	createBillboard("2", "London", london);
	createBillboard("3", "Tokyo", tokyo);
	createBillboard("4", "Paris", paris);
	createBillboard("5", "Rome", rome);
});

//create new POI/billboard
function createBillboard(id,title, location)
{
	var newBillboard = new arel.Object.POI();
    newBillboard.setID(id);
    newBillboard.setTitle(title);
    newBillboard.setLocation(location);
    newBillboard.setThumbnail("");
    newBillboard.setIcon("");
    newBillboard.setVisibility(true,false,true);
    arel.Scene.addObject(newBillboard);
}