<template>
  <div>
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark justify-content-between">
			<ul class="navbar-nav">
				<li class="nav-item">
					<a class="nav-link" href="#/login">Log In</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#/home">Home</a>
				</li>
        <li>
          <a class="nav-link" href="#/admin/add-certificate">Add new certificate</a>
        </li>
        <li class="nav-item">
					<a class="nav-link" href="#/admin/issued-certificates">Issued certificates</a>
        </li>
        <li class="nav-item">
					<a class="nav-link" href="#/admin/all-certificates">All certificates</a>
        </li>
      </ul>
		</nav> 
  </div>
</template>

<script>
  export default  {
    name: 'AdminNavbar',
    props: []
}
</script>
