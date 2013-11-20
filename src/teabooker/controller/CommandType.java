package teabooker.controller;

//CommandTypes for Servlet method parameter
public enum CommandType {
	// Null value
	None("none"),
	// Invalid value
	InValid("invaild"),
	// Create a new user
	CreateNewUser("creatnewuser"),
	// Get a user by user id
	GetUserById("getuserbyid"),
	// User login
	UserLogin("userlogin"),
	// Get a user by user name
	GetUserByName("getuserbyname"),
	// Update UserInfo
	UpdateUser("updateuser"),
	// Reset user password
	RestUserPass("resetuserpass"),
	// Create a new group
	CreateGroup("creategroup"),
	// Update GroupInfo
	UpdateGroup("updategroup"),
	// User join a group
	JoinGroup("joingroup"),
	// User join groups
	JoinGroups("joingroups"),
	// Update group user status
	UpdateGroupUserStatus("updategroupuserstatus"),
	// Get group by query
	GetGroupByQuery("getgroupbyquery"),
	// Get groups by user
	GetGroupsByUser("getgroupsbyuser"),
	// Get group members
	GetGroupMembers("getgroupmembers"),
	// Get group members by status
	GetGroupMemebersByStatus("getgroupmemebersbystatus"),
	// Get shops
	GetShops("getshops"),
	// Get shop address
	GetShopAddress("getshopaddress"),
	// Get shop food
	GetShopFood("getshopfood"),
	// Create new event
	CreateEvent("createevent"),
	// Get single event detail
	GetEvent("getevent"),
	// Get multiple events
	GetEvents("getevents"),
	// Get user's event
	GetUserEvents("getuserevents"),
	// Get event members
	GetEventMembers("geteventmembers"),
	// Update event status
	UpdateEventStatus("updateeventstatus"),
	// Update event user status
	UpdateEventUserStatus("updateeventuserstatus"),
	// Join event
	JoinEvent("joinevent"),
	// Create orders
	CreateOrders("createorders"),
	// Get orders by user.
	GetUserOrders("getuserorders"),
	// Get event user orders
	GetEventUserOrders("geteventuserorders"),
	// Update order status
	UpdateOrderStatus("updateorderstatus"),
	// Get event orders
	GetEventOrders("geteventorders");

	private String commandTxt;

	public String getCommandText() {
		return this.commandTxt;
	}

	CommandType(String commandText) {
		this.commandTxt = commandText;
	}

	// Convert string to enmu
	public static CommandType convertFormString(String text) {
		if (text != null) {
			for (CommandType c : CommandType.values()) {
				if (text.equalsIgnoreCase(c.commandTxt)) {
					return c;
				}
			}
			return CommandType.InValid;
		} else {
			return CommandType.None;
		}
	}

}
