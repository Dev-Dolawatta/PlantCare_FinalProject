// -------------------- User Registration --------------------
$("#registerForm").submit(function (e) {
    e.preventDefault();
    let user = {
        isActive: true,
        email: $("#email").val(),
        password: $("#password").val(),
        plantLimit: 10,
        username: $("#username").val()
    };
    $.ajax({
        url: "http://localhost:8080/api/users/add",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(user),
        success: function (response) {
            alert("User registered successfully!");
            window.location.href = "login.html";
        },
        error: function (xhr) {
            alert("Error: " + xhr.responseText);
        }
    });
});

// -------------------- User Login --------------------
$("#loginForm").submit(function (e) {
    e.preventDefault();
    let loginRequest = {
        email: $("#email").val(),
        password: $("#password").val()
    };
    $.ajax({
        url: "http://localhost:8080/api/users/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(loginRequest),
        success: function (response) {
            alert("Login successful! Welcome " + response.username);
            localStorage.setItem("userId", response.userId);
            window.location.href = "new.html";
        },
        error: function (xhr) {
            alert("Login failed: " + xhr.responseText);
        }
    });
});

// -------------------- Document Ready --------------------
$(document).ready(function () {
    const userId = localStorage.getItem("userId");

    // Redirect if no user is logged in
    if (!userId) {
        if ($("#profileForm").length || $("#addPlantForm").length || $("#plantGrid").length) {
            alert("No user logged in! Please login first.");
            window.location.href = "login.html";
            return;
        }
    }

    // -------------------- Add Plant --------------------
    $("#addPlantForm").submit(function (e) {
        e.preventDefault();
        if (!userId) {
            alert("No user logged in! Please login first.");
            return;
        }

        let formData = new FormData();
        formData.append("name", $("#plantName").val());
        formData.append("image", $("#plantImage")[0].files[0]);
        formData.append("environmentType", $("#plantEnvironment").val());
        formData.append("age", $("#plantAge").val());
        formData.append("type", $("#plantType").val());
        formData.append("healthStatus", $("#plantHealth").val());
        formData.append("priorityLvl", $("#plantPriority").val());
        formData.append("isFavourite", $("#isFavourite").is(":checked"));
        formData.append("isArchived", $("#isArchived").is(":checked"));

        $.ajax({
            url: "http://localhost:8080/plant/add/" + userId,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (plant) {
                alert("Plant saved successfully!");
                $("#addPlant").modal("hide");
                $("#addPlantForm")[0].reset();
                $("#plantGrid").append(appendPlantCard(plant));
            },
            error: function (xhr) {
                alert("Error saving plant: " + xhr.responseText);
            }
        });
    });

    // -------------------- Load Plants --------------------
    function loadPlants() {
        if (!userId) return;

        $.ajax({
            url: `http://localhost:8080/plant/active/${userId}`,
            type: "GET",
            success: function (plants) {
                console.log("=== DEBUGGING PLANT DATA ===");
                console.log("Total plants loaded:", plants.length);
                plants.forEach((plant, index) => {
                    console.log(`Plant ${index + 1}:`, {
                        name: plant.name,
                        image: plant.image,
                        imageUrl: `http://localhost:8080/plant/images/${plant.image}`,
                        hasImage: !!plant.image
                    });
                });
                console.log("=== END DEBUGGING ===");

                $("#plantGrid").empty();
                plants.forEach(appendPlantCard);
            },
            error: function () {
                alert("Failed to load plants.");
            }
        });
    }

    loadPlants();

    // -------------------- Test Image Endpoint --------------------
    function testImageEndpoint() {
        console.log("=== TESTING IMAGE ENDPOINT ===");
        // Test with a known image from the uploads folder using WebConfig path
        const testImageUrl = "http://localhost:8080/images/1757031080622_images (1).jpeg";
        console.log("Testing image URL:", testImageUrl);

        // Create a test image element
        const testImg = new Image();
        testImg.onload = function() {
            console.log("✅ Image loaded successfully!");
        };
        testImg.onerror = function() {
            console.log("❌ Image failed to load");
        };
        testImg.src = testImageUrl;
    }

    // Run the test after a short delay
    setTimeout(testImageEndpoint, 2000);

    // -------------------- Profile --------------------
    if ($("#profileForm").length) {
        $.ajax({
            url: "http://localhost:8080/api/users/" + userId,
            type: "GET",
            success: function (user) {
                $("#userId").val(user.userId);
                $("#username").val(user.username);
                $("#email").val(user.email);
                $("#password").val(user.password);
            },
            error: function () {
                alert("Failed to load profile details.");
            }
        });
    }

    $("#profileForm").submit(function (e) {
        e.preventDefault();
        let updatedUser = {
            userId: parseInt($("#userId").val()),
            username: $("#username").val(),
            email: $("#email").val(),
            password: $("#password").val(),
            isActive: true,
            plantLimit: 10
        };
        $.ajax({
            url: "http://localhost:8080/api/users/update/" + userId,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(updatedUser),
            success: function () {
                alert("Profile updated successfully!");
            },
            error: function (xhr) {
                alert("Error updating profile: " + xhr.responseText);
            }
        });
    });

    $("#deleteBtn").click(function () {
        if (confirm("⚠️ Are you sure you want to delete your account? This cannot be undone.")) {
            $.ajax({
                url: "http://localhost:8080/api/users/delete/" + userId,
                type: "DELETE",
                success: function () {
                    alert("Account deleted successfully!");
                    localStorage.removeItem("userId");
                    window.location.replace("register.html");
                },
                error: function (xhr) {
                    alert("Error deleting account: " + xhr.responseText);
                }
            });
        }
    });

    // -------------------- Plant Card --------------------
    function appendPlantCard(plant) {
        console.log("Creating plant card for:", plant.name, "Image:", plant.image); // Debug log

        // Use WebConfig path instead of controller path
        // const imageUrl = plant.image
        const imageUrl = plant.image ? `http://localhost:8080/images/${plant.image}` : 'https://via.placeholder.com/300x200?text=No+Image';
            // ? `http://localhost:8080/images/${encodeURIComponent(plant.image)}`
            // : 'https://via.placeholder.com/300x200?text=No+Image';

        return `
        <div class="col-md-4 mb-4"> 
          <div class="card shadow-sm"> 
            <img src="${imageUrl}" 
                 class="card-img-top" 
                 alt="${plant.name}"
                 style="height: 200px; object-fit: cover;"
                 onerror="console.error('Failed to load image:', this.src); this.src='https://via.placeholder.com/300x200?text=Image+Not+Found'">
            <div class="card-body"> 
              <h5 class="card-title d-flex justify-content-between align-items-center"> 
                ${plant.name} ${plant.isFavourite ? '<span class="text-warning">⭐</span>' : ''} 
              </h5> 
              <p class="card-text"> 
                Type: ${plant.type} <br> 
                Status: <span class="badge bg-${plant.healthStatus === 'HEALTHY' ? 'success' : 'warning'}">${plant.healthStatus}</span><br> 
                Priority: <span class="badge bg-${plant.priorityLvl === 'HIGH' ? 'danger' : (plant.priorityLvl === 'MEDIUM' ? 'primary' : 'secondary')}">${plant.priorityLvl}</span> 
              </p> 
              <div class="d-flex justify-content-between"> 
                <a href="plant-details.html?id=${plant.plantId}" class="btn btn-sm btn-outline-success">View</a> 
                <button class="btn btn-sm btn-outline-primary">Edit</button> 
                <button class="btn btn-sm btn-outline-danger archive-btn" data-id="${plant.plantId}">Archive</button> 
              </div> 
            </div> 
          </div> 
        </div>`;
    }


    // -------------------- Archive Button --------------------
    $(document).on("click", ".archive-btn", function () {
        const plantId = $(this).data("id");
        $.ajax({
            url: `http://localhost:8080/plant/${plantId}/archive`,
            type: "PUT",
            success: function () {
                alert("Plant archived successfully!");
                window.location.href = "archive.html";
            },
            error: function () {
                alert("Failed to archive plant.");
            }
        });
    });

    // -------------------- Plant Counts --------------------
    function updatePlantCounts(userId) {
        if (!userId) return;
        $.ajax({
            url: `http://localhost:8080/plant/counts/${userId}`,
            type: 'GET',
            success: function(data) {
                $('#totalPlants').text(data.totalPlants);
                $('#favPlants').text(data.favouritePlants);
            },
            error: function(err) {
                console.error('Error fetching plant counts:', err);
            }
        });
    }

    updatePlantCounts(userId);

    // -------------------- Archived Plants --------------------
    function loadArchivedPlants() {
        $.ajax({
            url: `http://localhost:8080/plant/archived/${userId}`,
            type: "GET",
            success: function (plants) {
                const $tbody = $("#archivedTable tbody");
                $tbody.empty();
                if (plants.length > 0) {
                    plants.forEach(plant => {
                        $tbody.append(`
                            <tr>
                                <td>${plant.name}</td>
                                <td>${plant.type}</td>
                                <td>${plant.age || ''}</td>
                                <td>${plant.environmentType || ''}</td>
                                <td>
                                    <button class="btn btn-sm btn-warning restore-btn" data-id="${plant.plantId}">Restore</button>
                                    <button class="btn btn-sm btn-danger delete-btn" data-id="${plant.plantId}">Delete</button>
                                </td>
                            </tr>
                        `);
                    });
                } else {
                    $tbody.append(`<tr><td colspan="5" class="text-center">No archived plants</td></tr>`);
                }
            },
            error: function (xhr) {
                console.error(xhr.responseText);
                alert('Failed to load archived plants.');
            }
        });
    }

    loadArchivedPlants();

    // Restore Plant
    $(document).on("click", ".restore-btn", function () {
        const plantId = $(this).data("id");
        $.ajax({
            url: `http://localhost:8080/plant/${plantId}/restore`,
            type: "PUT",
            success: function () {
                alert("Plant restored!");
                loadArchivedPlants();
            },
            error: function () {
                alert("Failed to restore plant.");
            }
        });
    });

    // Delete Plant
    $(document).on("click", ".delete-btn", function () {
        const plantId = $(this).data("id");
        if (!confirm("Are you sure you want to delete this plant permanently?")) return;
        $.ajax({
            url: `http://localhost:8080/plant/delete/${plantId}`,
            type: "DELETE",
            success: function () {
                alert("Plant deleted!");
                location.reload();
            },
            error: function () {
                alert("Failed to delete plant.");
            }
        });
    });

    // -------------------- Search & Filter Plants --------------------
    function renderPlants(plants) {
        const plantGrid = $("#plantGrid");
        plantGrid.empty();
        if (!plants || plants.length === 0) {
            plantGrid.append('<p class="text-muted">No plants found.</p>');
            return;
        }
        plants.forEach(plant => {
            plantGrid.append(appendPlantCard(plant));
        });
        updatePlantCounts(userId);
    }
    function loadAllPlants() {
        $.ajax({
            url: `http://localhost:8080/plant/all/${userId}`,
            type: "GET",
            success: renderPlants,
            error: function (xhr, status, error) {
                console.error("Error loading plants:", error);
            }
        });
    }

    $("#filterName").on("input", function () {
        const name = $(this).val();
        if (name === "") {
            loadAllPlants();
            return;
        }
        $.ajax({
            url: `http://localhost:8080/plant/search/name/${userId}`,
            type: "GET",
            data: { name: name },
            success: renderPlants,
            error: function (xhr, status, error) {
                console.error("Error searching plants:", error);
            }
        });
    });

    $("#filterType").on("change", function () {
        const type = $(this).val();
        if (type === "") {
            loadAllPlants();
            return;
        }
        $.ajax({
            url: `http://localhost:8080/plant/type/${userId}`,
            type: "GET",
            data: { type: type },
            success: renderPlants,
            error: function (xhr, status, error) {
                console.error("Error fetching plants by type:", error);
            }
        });
    });

    loadAllPlants();

    $(document).ready(function () {

        /////////////////////////////////////////////////////////// Load Plants into Dropdown
        function loadUserPlants() {
            $.ajax({
                url: `http://localhost:8080/plant/active/${userId}`,
                method: "GET",
                success: function (plants) {
                    const $select = $("#plantSelect");
                    $select.empty();
                    if (!plants || plants.length === 0) {
                        $select.append('<option value="">No active plants found</option>');
                    } else {
                        $select.append('<option value="">Select a plant</option>');
                        plants.forEach(p => {
                            $select.append(`<option value="${p.plantId}">${p.name}</option>`);
                        });
                    }
                }
            });
        }

        $('#taskModal').on('show.bs.modal', loadUserPlants);

        /////////////////////////////////////////////////////////// Add New Task
        function addTaskHandler(e) {
            e.preventDefault();
            const plantId = parseInt($('#plantSelect').val());
            if (!plantId) {
                alert('Please select a plant.');
                return;
            }

            const newTask = {
                taskType: $('#taskType').val(),
                scheduledDate: $('#scheduledDate').val(),
                priority: $('#priority').val(),
                plant: { plantId: plantId }
            };

            $.ajax({
                url: 'http://localhost:8080/api/tasks',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(newTask),
                success: function () {
                    alert('Task added successfully!');
                    $('#taskModal').modal('hide');
                    $('#taskForm')[0].reset();
                    loadActiveTasks();
                    loadCareHistoryTasks();
                },
                error: function (xhr) {
                    console.error(xhr.responseText);
                    alert('Failed to save task.');
                }
            });
        }

        $('#taskForm').off('submit').on('submit', addTaskHandler);

        /////////////////////////////////////////////////////////// Load Active Tasks
        function loadActiveTasks() {
            $.ajax({
                url: `http://localhost:8080/api/tasks/user/${userId}`,
                method: 'GET',
                success: function (tasks) {
                    const $tableBody = $('#tasksTable tbody');
                    $tableBody.empty();

                    tasks.forEach(task => {
                        if (task.completedDate) return; // Skip completed tasks

                        const priorityBadge = task.priority === 'HIGH' ? 'danger' :
                            task.priority === 'MEDIUM' ? 'warning' : 'success';

                        $tableBody.append(`
            <tr>
              <td>${task.plant?.name || 'Unknown'}</td>
              <td>${task.taskType}</td>
              <td>${task.scheduledDate || 'N/A'}</td>
              <td>${task.nextDueDate || ''}</td>
              <td><span class="badge bg-${priorityBadge}">${task.priority}</span></td>
              <td><span class="badge bg-warning">Pending</span></td>
              <td class="text-center">
                <button class="btn btn-sm btn-success complete-btn" data-id="${task.taskId}">✅ Done</button>
                <button class="btn btn-sm btn-danger delete-btn" data-id="${task.taskId}">🗑 Delete</button>
              </td>
            </tr>
          `);
                    });
                },
                error: function () {
                    alert('Failed to load active tasks.');
                }
            });
        }

        /////////////////////////////////////////////////////////// Load Care History
        function loadCareHistoryTasks() {
            $.ajax({
                url: `http://localhost:8080/api/tasks/user/${userId}`,
                method: 'GET',
                success: function (tasks) {
                    const $taskList = $('#taskList');
                    $taskList.empty();

                    if (!tasks || tasks.length === 0) {
                        $taskList.append('<p class="text-muted">No care tasks found.</p>');
                        return;
                    }

                    tasks.forEach(task => {
                        const priorityClass = task.priority === 'HIGH' ? 'priority-high' :
                            task.priority === 'MEDIUM' ? 'priority-medium' : 'priority-low';

                        const statusBadge = task.completedDate
                            ? '<span class="badge bg-success">Completed</span>'
                            : '<span class="badge bg-warning">Pending</span>';

                        $taskList.append(`
            <div class="task-item d-flex align-items-center justify-content-between mb-2">
              <div class="task-priority ${priorityClass}"></div>
              <div class="task-content flex-grow-1 ms-2">
                <div class="task-title fw-semibold">${task.taskType}</div>
                <div class="task-details text-muted">
                  ${task.plant ? task.plant.name : 'Unknown Plant'}<br>
                  Scheduled: ${task.scheduledDate || 'N/A'}<br>
                  Completed: ${task.completedDate || 'Pending'}
                </div>
              </div>
              <div class="task-date text-end">
                ${statusBadge}
              </div>
            </div>
          `);
                    });
                },
                error: function () {
                    alert('Failed to load care history tasks.');
                }
            });
        }

        /////////////////////////////////////////////////////////// Delete Task
        $('#tasksTable').on('click', '.delete-btn', function () {
            const taskId = $(this).data('id');
            if (!confirm('Are you sure you want to delete this task?')) return;

            $.ajax({
                url: `http://localhost:8080/api/tasks/${taskId}`,
                type: 'DELETE',
                success: function () {
                    loadActiveTasks();
                    loadCareHistoryTasks();
                },
                error: function (xhr) {
                    console.error(xhr.responseText);
                    alert('Failed to delete task.');
                }
            });
        });

        /////////////////////////////////////////////////////////// Complete Task
        $(document).on("click", ".complete-btn", function () {
            const taskId = $(this).data("id");
            if (!confirm("Mark this task as completed?")) return;

            $.ajax({
                url: `http://localhost:8080/api/tasks/${taskId}/complete`,
                type: "PUT",
                success: function () {
                    alert("Task marked as completed!");
                    loadActiveTasks();
                    loadCareHistoryTasks();
                },
                error: function () {
                    alert("Failed to complete task.");
                }
            });
        });

        /////////////////////////////////////////////////////////// Initial Load
        loadActiveTasks();
        loadCareHistoryTasks();

    });
    $('#logoutBtn').on('click', function () {
        if (!confirm("Are you sure you want to logout?")) return;

        $.ajax({
            url: 'http://localhost:8080/api/auth/logout',
            type: 'POST',
            success: function () {

                localStorage.clear();
                sessionStorage.clear();

                alert("You have been logged out.");
                window.location.href = "/login.html";
            },
            error: function () {
                alert("Logout failed.");
            }
        });
    });

});



