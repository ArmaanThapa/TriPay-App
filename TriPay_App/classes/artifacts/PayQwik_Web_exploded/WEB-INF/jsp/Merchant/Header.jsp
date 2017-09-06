<nav class="navbar">
	<div class="col-md-12">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${pageContext.request.contextPath}/Merchant/Home">PayQwik</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-right active">
				<li id="Home"><a href="${pageContext.request.contextPath}/Merchant/Home">Home</a></li>
				<li class="dropdown" id="${pageContext.request.contextPath}/Merchant/Account"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown">Account</a>
					<ul class="dropdown-menu scroll-menu">
						<li><a href="${pageContext.request.contextPath}/Merchant/EditProfile">Edit Profile</a></li>
						<li><a href="${pageContext.request.contextPath}/Merchant/ChangePassword">Change Password</a></li>
					</ul></li>
				<li><a href="/logout">Logout</a></li>
			</ul>
		</div>
	</div>
</nav>