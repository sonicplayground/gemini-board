from typing import List, Optional
from uuid import UUID

from fastapi import Depends, FastAPI, HTTPException, Query, Security
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from pydantic import BaseModel, Field
from starlette import status
from starlette.requests import Request

# Dummy data and functions for demonstration purposes
# Replace these with your actual database interactions and business logic

class Vehicle:
  def __init__(self, vehicle_key: UUID, vin: str, owner_user_key: str,
      manufacturer: str, model_name: str, mileage: int, tire_position: Optional[str] = None):
    self.vehicle_key = vehicle_key
    self.vin = vin
    self.owner_user_key = owner_user_key
    self.manufacturer = manufacturer
    self.model_name = model_name
    self.mileage = mileage
    self.tire_position = tire_position

class VehicleSearchCondition(BaseModel):
  vin: Optional[str] = None
  owner_user_key: Optional[str] = None
  manufacturer: Optional[str] = None
  model_name: Optional[str] = None
  vehicle_key: Optional[str] = None
  min_mileage: Optional[int] = None
  max_mileage: Optional[int] = None

class CreateVehicleRequest(BaseModel):
  vin: str = Field(..., description="Vehicle Identification Number")
  manufacturer: str = Field(..., description="Vehicle Manufacturer")
  model_name: str = Field(..., description="Vehicle Model Name")
  mileage: int = Field(..., description="Vehicle Mileage")

class UpdateVehicleRequest(BaseModel):
  vin: Optional[str] = None
  manufacturer: Optional[str] = None
  model_name: Optional[str] = None
  mileage: Optional[int] = None

class MaintenanceRequest(BaseModel):
  maintenance_type: str = Field(..., description="Type of maintenance (e.g., 'tire', 'oil')")
  change_date: str = Field(..., description="Date of maintenance")
  tire_position: Optional[str] = Field(None, description="Tire position (e.g., 'front-left', 'rear-right')")

class UpdateMileageRequest(BaseModel):
  mileage: int = Field(..., description="New mileage value")

class VehicleResponse(BaseModel):
  vehicle_key: UUID
  vin: str
  owner_user_key: str
  manufacturer: str
  model_name: str
  mileage: int
  tire_position: Optional[str] = None

class CreateVehicleResponse(BaseModel):
  vehicle_key: UUID

class ResultMessageResponse(BaseModel):
  message: str

class PagedContent(BaseModel):
  content: List[VehicleResponse]
  total_pages: int
  total_elements: int
  page_number: int
  page_size: int

# Dummy data
vehicles_db: List[Vehicle] = []
next_vehicle_key = 1

# Dummy authentication
security = HTTPBearer()

def get_current_user(credentials: HTTPAuthorizationCredentials = Security(security)):
  # Replace this with your actual authentication logic
  # This is a dummy implementation for demonstration
  if credentials.credentials == "valid_token_admin":
    return {"username": "admin", "authorities": ["SERVICE_ADMIN"]}
  elif credentials.credentials == "valid_token_user":
    return {"username": "user123", "authorities": ["SERVICE_USER"]}
  else:
    raise HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Invalid authentication credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )

# Dummy application service
class VehicleApplicationService:
  def create_vehicle(self, request: CreateVehicleRequest, owner_user_key: str) -> UUID:
    global next_vehicle_key
    vehicle_key = UUID(int=next_vehicle_key)
    next_vehicle_key += 1
    vehicle = Vehicle(vehicle_key, request.vin, owner_user_key, request.manufacturer, request.model_name, request.mileage)
    vehicles_db.append(vehicle)
    return vehicle_key

  def get_vehicle(self, requester_info: dict, vehicle_key: UUID) -> VehicleResponse:
    vehicle = next((v for v in vehicles_db if v.vehicle_key == vehicle_key), None)
    if not vehicle:
      raise HTTPException(status_code=404, detail="Vehicle not found")
    return VehicleResponse(vehicle_key=vehicle.vehicle_key, vin=vehicle.vin, owner_user_key=vehicle.owner_user_key, manufacturer=vehicle.manufacturer, model_name=vehicle.model_name, mileage=vehicle.mileage, tire_position=vehicle.tire_position)

  def get_vehicles(self, requester_info: dict, condition: VehicleSearchCondition, page: int, size: int) -> PagedContent:
    filtered_vehicles = [
      v
      for v in vehicles_db
      if (condition.vin is None or v.vin == condition.vin)
         and (condition.owner_user_key is None or v.owner_user_key == condition.owner_user_key)
         and (condition.manufacturer is None or v.manufacturer == condition.manufacturer)
         and (condition.model_name is None or v.model_name == condition.model_name)
         and (condition.vehicle_key is None or str(v.vehicle_key) == condition.vehicle_key)
         and (condition.min_mileage is None or v.mileage >= condition.min_mileage)
         and (condition.max_mileage is None or v.mileage <= condition.max_mileage)
    ]

    start = page * size
    end = start + size
    paginated_vehicles = filtered_vehicles[start:end]

    vehicle_responses = [VehicleResponse(vehicle_key=v.vehicle_key, vin=v.vin, owner_user_key=v.owner_user_key, manufacturer=v.manufacturer, model_name=v.model_name, mileage=v.mileage, tire_position=v.tire_position) for v in paginated_vehicles]

    return PagedContent(content=vehicle_responses, total_pages=(len(filtered_vehicles) + size - 1) // size, total_elements=len(filtered_vehicles), page_number=page, page_size=size)

  def update_vehicle(self, requester_info: dict, vehicle_key: UUID, request: UpdateVehicleRequest) -> VehicleResponse:
    vehicle = next((v for v in vehicles_db if v.vehicle_key == vehicle_key), None)
    if not vehicle:
      raise HTTPException(status_code=404, detail="Vehicle not found")
    if request.vin is not None:
      vehicle.vin = request.vin
    if request.manufacturer is not None:
      vehicle.manufacturer = request.manufacturer
    if request.model_name is not None:
      vehicle.model_name = request.model_name
    if request.mileage is not None:
      vehicle.mileage = request.mileage
    return VehicleResponse(vehicle_key=vehicle.vehicle_key, vin=vehicle.vin, owner_user_key=vehicle.owner_user_key, manufacturer=vehicle.manufacturer, model_name=vehicle.model_name, mileage=vehicle.mileage, tire_position=vehicle.tire_position)

  def replace_equipment(self, requester_info: dict, vehicle_key: UUID, maintenance_type: str, change_date: str, tire_position: Optional[str] = None) -> None:
    vehicle = next((v for v in vehicles_db if v.vehicle_key == vehicle_key), None)
    if not vehicle:
      raise HTTPException(status_code=404, detail="Vehicle not found")
    if maintenance_type == "tire":
      vehicle.tire_position = tire_position
    # Add other maintenance logic here

  def update_mileage(self, requester_info: dict, vehicle_key: UUID, mileage: int) -> None:
    vehicle = next((v for v in vehicles_db if v.vehicle_key == vehicle_key), None)
    if not vehicle:
      raise HTTPException(status_code=404, detail="Vehicle not found")
    vehicle.mileage = mileage

  def delete_vehicle(self, requester_info: dict, vehicle_key: UUID) -> None:
    global vehicles_db
    vehicles_db = [v for v in vehicles_db if v.vehicle_key != vehicle_key]

app = FastAPI()
vehicle_application_service = VehicleApplicationService()

def has_authority(required_authority: str):
  def dependency(current_user: dict = Depends(get_current_user)):
    if required_authority not in current_user["authorities"]:
      raise HTTPException(
          status_code=status.HTTP_403_FORBIDDEN,
          detail=f"Insufficient permissions. Requires {required_authority} authority.",
      )
    return current_user
  return dependency

@app.post("/api/v1/vehicles", response_model=CreateVehicleResponse, status_code=status.HTTP_200_OK)
def create_vehicle(
    request: CreateVehicleRequest,
    current_user: dict = Depends(has_authority("SERVICE_USER")),
):
  vehicle_key = vehicle_application_service.create_vehicle(request, current_user["username"])
  return CreateVehicleResponse(vehicle_key=vehicle_key)

@app.get("/api/v1/vehicles/{vehicle_key}", response_model=VehicleResponse, status_code=status.HTTP_200_OK)
def get_vehicle(
    vehicle_key: UUID,
    current_user: dict = Depends(get_current_user),
):
  requester_info = current_user
  return vehicle_application_service.get_vehicle(requester_info, vehicle_key)

@app.get("/api/v1/vehicles", response_model=PagedContent, status_code=status.HTTP_200_OK)
def get_vehicles(
    request: Request,
    page: int = Query(0, ge=0),
    size: int = Query(10, ge=1, le=100),
    vin: Optional[str] = Query(None),
    owner_user_key: Optional[str] = Query(None),
    manufacturer: Optional[str] = Query(None),
    model_name: Optional[str] = Query(None),
    vehicle_key: Optional[str] = Query(None),
    min_mileage: Optional[int] = Query(None),
    max_mileage: Optional[int] = Query(None),
    current_user: dict = Depends(has_authority("SERVICE_ADMIN") or has_authority("SERVICE_USER")),
):
  requester_info = current_user
  condition = VehicleSearchCondition(
      vin=vin,
      owner_user_key=owner_user_key,
      manufacturer=manufacturer,
      model_name=model_name,
      vehicle_key=vehicle_key,
      min_mileage=min_mileage,
      max_mileage=max_mileage,
  )
  return vehicle_application_service.get_vehicles(requester_info, condition, page, size)

@app.put("/api/v1/vehicles/{vehicle_key}", response_model=VehicleResponse, status_code=status.HTTP_200_OK)
def update_vehicle(
    vehicle_key: UUID,
    request: UpdateVehicleRequest,
    current_user: dict = Depends(has_authority("SERVICE_ADMIN") or has_authority("SERVICE_USER")),
):
  requester_info = current_user
  return vehicle_application_service.update_vehicle(requester_info, vehicle_key, request)

@app.patch("/api/v1/vehicles/{vehicle_key}/maintenance", response_model=ResultMessageResponse, status_code=status.HTTP_200_OK)
def update_maintenance(
    vehicle_key: UUID,
    request: MaintenanceRequest,
    current_user: dict = Depends(has_authority("SERVICE_ADMIN") or has_authority("SERVICE_USER")),
):
  requester_info = current_user
  vehicle_application_service.replace_equipment(requester_info, vehicle_key, request.maintenance_type, request.change_date, request.tire_position)
  return ResultMessageResponse(message="success")

@app.patch("/api/v1/vehicles/{vehicle_key}/mileage", response_model=ResultMessageResponse, status_code=status.HTTP_200_OK)
def update_mileage(
    vehicle_key: UUID,
    request: UpdateMileageRequest,
    current_user: dict = Depends(has_authority("SERVICE_ADMIN") or has_authority("SERVICE_USER")),
):
  requester_info = current_user
  vehicle_application_service.update_mileage(requester_info, vehicle_key, request.mileage)
  return ResultMessageResponse(message="success")

@app.delete("/api/v1/vehicles/{vehicle_key}", response_model=ResultMessageResponse, status_code=status.HTTP_200_OK)
def delete_vehicle(
    vehicle_key: UUID,
    current_user: dict = Depends(get_current_user),
):
  requester_info = current_user
  vehicle_application_service.delete_vehicle(requester_info, vehicle_key)
  return ResultMessageResponse(message="success")